package pl.devcezz.barentswatch.api;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import pl.devcezz.barentswatch.Registry;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/barentswatch")
public class BarentsWatchResource {

    @Inject
    @RestClient
    BarentsWatchExternalApi barentsWatchExternalApi;

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/vessel/position")
    public List<OpenPosition> getVesselsPositionFor(BoundingBoxRequest request) {
        return barentsWatchExternalApi.getVesselsPositionsFor(
                Registry.accessToken.get(),
                request.xMin(),
                request.xMax(),
                request.yMin(),
                request.yMax());
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/vessel/position/{mmsi}")
    public OpenPosition getVesselsPositionFor(@PathParam(value = "mmsi") Integer mmsi) {
        return barentsWatchExternalApi.getVesselPositionFor(Registry.accessToken.get(), mmsi);
    }
}

record BoundingBoxRequest(double xMin, double xMax, double yMin, double yMax) {}