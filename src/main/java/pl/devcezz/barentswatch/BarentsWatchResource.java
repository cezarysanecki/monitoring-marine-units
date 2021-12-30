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
    @Path("/vessels")
    public String getAllVessels() {
        return barentsWatchService.getAllVessels();
    }
}