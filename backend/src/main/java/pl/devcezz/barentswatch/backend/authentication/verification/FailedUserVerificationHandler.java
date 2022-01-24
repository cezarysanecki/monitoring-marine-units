package pl.devcezz.barentswatch.backend.authentication.verification;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class FailedUserVerificationHandler implements ExceptionMapper<FailedUserVerificationException> {

    @Override
    public Response toResponse(FailedUserVerificationException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new Issue(exception.getMessage()))
                .build();
    }
}

record Issue(String error) {}
