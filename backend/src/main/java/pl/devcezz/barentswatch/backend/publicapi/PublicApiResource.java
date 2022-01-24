package pl.devcezz.barentswatch.backend.publicapi;

import pl.devcezz.barentswatch.backend.common.Area;
import pl.devcezz.barentswatch.backend.common.VesselRegistry;
import pl.devcezz.barentswatch.backend.externalapi.BarentsWatchFacade;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Comparator;
import java.util.List;

@Path("/barentswatch/public")
public class PublicApiResource {

    @Inject
    BarentsWatchFacade barentsWatchFacade;

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/vessels")
    public List<VesselRegistry> getVessels(@QueryParam(value = "xMin") Double xMin,
                                           @QueryParam(value = "xMax") Double xMax,
                                           @QueryParam(value = "yMin") Double yMin,
                                           @QueryParam(value = "yMax") Double yMax) {
        return barentsWatchFacade.getVesselsRegistryFor(new Area(xMin, xMax, yMin, yMax)).stream()
                .sorted(Comparator.comparing(VesselRegistry::mmsi))
                .toList();
    }
}