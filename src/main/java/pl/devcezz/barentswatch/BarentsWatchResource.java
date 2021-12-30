package pl.devcezz.barentswatch;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/barentswatch")
public class BarentsWatchResource {

    private final BarentsWatchService barentsWatchService;

    BarentsWatchResource(BarentsWatchService barentsWatchService) {
        this.barentsWatchService = barentsWatchService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/vessels/positions")
    public String getVesselsPositionFor(BoundingBox boundingBox) {
        return barentsWatchService.getVesselsPositionFor(boundingBox);
    }
}

record BoundingBox(double xMin, double xMax, double yMin, double yMax) {}