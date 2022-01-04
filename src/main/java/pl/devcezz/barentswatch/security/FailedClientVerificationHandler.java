package pl.devcezz.barentswatch.security;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class FailedClientVerificationHandler implements ExceptionMapper<FailedClientVerificationException> {

    @Override
    public Response toResponse(FailedClientVerificationException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new Issue(exception.getMessage()))
                .build();
    }
}

record Issue(String error) {}
