package pl.devcezz.barentswatch.backend.vessel;

import pl.devcezz.barentswatch.backend.common.Coordinates;
import pl.devcezz.barentswatch.backend.common.VesselRegistry;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/barentswatch/vessel")
public class VesselResource {

    @Inject
    VesselService vesselService;

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/position")
    public List<VesselPointResponse> getVesselsPositionFor(@QueryParam(value = "xMin") Double xMin,
                                                           @QueryParam(value = "xMax") Double xMax,
                                                           @QueryParam(value = "yMin") Double yMin,
                                                           @QueryParam(value = "yMax") Double yMax) {
        return vesselService.fetchVesselsPositionsFor(new Area(xMin, xMax, yMin, yMax)).stream()
                .map(this::convertToResponse)
                .toList();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/position/{mmsi}")
    @RolesAllowed({ "user" })
    public VesselPointResponse getVesselsPositionFor(@PathParam(value = "mmsi") Integer mmsi) {
        return convertToResponse(vesselService.fetchVesselPosition(mmsi));
    }

    private VesselPointResponse convertToResponse(VesselRegistry registry) {
        return new VesselPointResponse(
                registry.timestamp().toString(),
                registry.mmsi(),
                registry.coordinates()
        );
    }
}

record VesselPointResponse(String timestamp, Integer mmsi, Coordinates coordinates) {}