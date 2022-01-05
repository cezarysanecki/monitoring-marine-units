package pl.devcezz.barentswatch.backend;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class BarentsWatchExceptionHandler implements ExceptionMapper<BarentsWatchException> {

    @Override
    public Response toResponse(BarentsWatchException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new Error(exception.getMessage()))
                .build();
    }
}

record Error(String message) {}