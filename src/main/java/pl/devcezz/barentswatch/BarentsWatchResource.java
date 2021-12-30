package pl.devcezz.barentswatch;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/barentswatch")
public class BarentsWatchResource {

    @Inject
    @RestClient
    BarentsWatchExternalApi barentsWatchExternalApi;

    @Inject
    @RestClient
    TokenExternalApi tokenExternalApi;

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/vessels/positions")
    public List<OpenPosition> getVesselsPositionFor(BoundingBoxRequest request) {
        return barentsWatchExternalApi.getVesselsPositionsFor(
                "Bearer xxx",
                request.xMin(),
                request.xMax(),
                request.yMin(),
                request.yMax());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/token")
    public String fetchToken() {
        Form form = new Form()
                .param("client_id", "xxx")
                .param("scope", "api")
                .param("client_secret", "xxx")
                .param("grant_type", "client_credentials");

        return tokenExternalApi.fetchToken(form);
    }
}

record BoundingBoxRequest(double xMin, double xMax, double yMin, double yMax) {}