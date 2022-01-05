package pl.devcezz.barentswatch.user;

import org.eclipse.microprofile.jwt.JsonWebToken;
import pl.devcezz.barentswatch.user.entity.UserVessel;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/barentswatch/user")
public class UserVesselsResource {

    @Inject
    UserVesselRepository userVesselRepository;

    @Inject
    JsonWebToken token;

    @POST
    @Path("/track")
    @RolesAllowed({ "user" })
    public void addVesselToTrack(Integer mmsi) {
        UserVessel userVessel = userVesselRepository.find("email", token.getSubject()).firstResult();
        userVessel.trackVessel(mmsi);
        userVesselRepository.update(userVessel);
    }

    @DELETE
    @Path("/track/suspend")
    @RolesAllowed({ "user" })
    public void suspendTrackingVessel(Integer mmsi) {
        UserVessel userVessel = userVesselRepository.find("email", token.getSubject()).firstResult();
        userVessel.suspendTrackingVessel(mmsi);
        userVesselRepository.update(userVessel);
    }

    @DELETE
    @Path("/track")
    @RolesAllowed({ "user" })
    public void removeTrackingVessel(Integer mmsi) {
        UserVessel userVessel = userVesselRepository.find("email", token.getSubject()).firstResult();
        userVessel.removeTrackingVessel(mmsi);
        userVesselRepository.update(userVessel);
    }
}