package pl.devcezz.barentswatch;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/barentswatch")
public class BarentsWatchResource {

    @Inject
    @RestClient
    BarentsWatchExternalApi barentsWatchExternalApi;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/vessels/positions")
    public List<OpenPosition> getVesselsPositionFor(BoundingBox boundingBox) {
        return barentsWatchExternalApi.method(
                "Bearer xxx",
                boundingBox.xMin(),
                boundingBox.xMax(),
                boundingBox.yMin(),
                boundingBox.yMax());
    }
}

record BoundingBox(double xMin, double xMax, double yMin, double yMax) {}