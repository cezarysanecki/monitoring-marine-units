package pl.devcezz.barentswatch.backend.monitoring;

import org.eclipse.microprofile.jwt.JsonWebToken;
import pl.devcezz.barentswatch.backend.common.UserMonitoring;
import pl.devcezz.barentswatch.backend.common.VesselToTrack;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/barentswatch/monitoring")
public class MonitoringResource {

    @Inject
    MonitoringService monitoringService;

    @Inject
    JsonWebToken token;

    @GET
    @Path("/vessels")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"user"})
    public List<UserMonitoring> getTrackedVessels() {
        return monitoringService.getTackedVessels(token.getSubject());
    }

    @POST
    @Path("/track")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"user"})
    public void trackVessel(VesselToTrack vessel) {
        monitoringService.trackVessel(token.getSubject(), vessel);
    }

    @DELETE
    @Path("/track/suspend")
    @RolesAllowed({"user"})
    public void suspendTrackingVessel(Integer mmsi) {
        monitoringService.suspendTracking(token.getSubject(), mmsi);
    }

    @DELETE
    @Path("/track")
    @RolesAllowed({"user"})
    public void removeTrackingVessel(Integer mmsi) {
        monitoringService.removeTracking(token.getSubject(), mmsi);
    }
}