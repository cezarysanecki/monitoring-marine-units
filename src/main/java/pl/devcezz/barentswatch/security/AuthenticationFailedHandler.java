package pl.devcezz.barentswatch.security;

import io.quarkus.arc.Priority;
import io.quarkus.security.AuthenticationFailedException;
import io.smallrye.jwt.auth.principal.ParseException;
import org.jose4j.jwt.consumer.InvalidJwtException;

import javax.ws.rs.Priorities;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFailedHandler implements ExceptionMapper<AuthenticationFailedException> {

    @Override
    public Response toResponse(AuthenticationFailedException exception) {
        var response = Response.status(Response.Status.UNAUTHORIZED)
                .header("WWW-Authenticate", "Bearer error=\"invalid_token\"");

        if (exception.getCause() instanceof ParseException parseException) {
            if (parseException.getCause() instanceof InvalidJwtException invalidJwtException) {
                List<String> errorMessages = invalidJwtException.getErrorDetails().stream()
                        .map(error -> ErrorCode.findErrorCodeBy(error.getErrorCode()))
                        .filter(Objects::nonNull)
                        .map(errorCode -> errorCode.message)
                        .collect(Collectors.toList());
                response.entity(new AuthenticationResponse(errorMessages));
            }
        }

        return response.build();
    }
}

record AuthenticationResponse(List<String> errors) {}

enum ErrorCode {
    INVALID_TOKEN(9, "invalid_token"),
    EXPIRED_TOKEN(1, "expired_token"),
    UNPARSABLE_TOKEN(17, "unparsable_token");

    ErrorCode(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    int errorCode;
    String message;

    static ErrorCode findErrorCodeBy(int code) {
        return Stream.of(values())
                .filter(errorCode -> errorCode.errorCode == code)
                .findFirst()
                .orElse(null);
    }
}