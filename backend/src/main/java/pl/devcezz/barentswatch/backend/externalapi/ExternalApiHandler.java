package pl.devcezz.barentswatch.backend.externalapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ExternalApiHandler implements ExceptionMapper<WebApplicationException> {

    Logger logger = LoggerFactory.getLogger(ExternalApiHandler.class);

    @Override
    public Response toResponse(WebApplicationException exception) {
        var status = extractHttpStatusCodeFrom(exception);
        return Response.status(status).build();
    }

    private Response.Status extractHttpStatusCodeFrom(WebApplicationException exception) {
        if (exception.getResponse().getStatus() == Response.Status.UNAUTHORIZED.getStatusCode()) {
            logger.error("Unauthorized call to external API");
            return Response.Status.INTERNAL_SERVER_ERROR;
        }
        return Response.Status.UNAUTHORIZED;
    }
}
