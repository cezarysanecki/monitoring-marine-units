package pl.devcezz.barentswatch.vessel;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import pl.devcezz.barentswatch.Registry;
import pl.devcezz.barentswatch.externalapi.BarentsWatchExternalApi;
import pl.devcezz.barentswatch.externalapi.OpenPosition;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/barentswatch/vessel")
public class VesselResource {

    @Inject
    @RestClient
    BarentsWatchExternalApi barentsWatchExternalApi;

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/position")
    public List<OpenPosition> getVesselsPositionFor(BoundingBoxRequest request) {
        return barentsWatchExternalApi.getVesselsPositionsFor(
                Registry.accessToken.get(),
                request.xMin(),
                request.xMax(),
                request.yMin(),
                request.yMax());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/position/{mmsi}")
    @RolesAllowed({ "user" })
    public OpenPosition getVesselsPositionFor(@PathParam(value = "mmsi") Integer mmsi) {
        return barentsWatchExternalApi.getVesselPositionFor(Registry.accessToken.get(), mmsi);
    }
}

record BoundingBoxRequest(double xMin, double xMax, double yMin, double yMax) {}