package pl.devcezz.barentswatch.backend.app;

import pl.devcezz.barentswatch.backend.app.exceptions.ApplicationException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ApplicationExceptionHandler implements ExceptionMapper<ApplicationException> {

    @Override
    public Response toResponse(ApplicationException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new Error(exception.getMessage()))
                .build();
    }

    record Error(String message) {}
}