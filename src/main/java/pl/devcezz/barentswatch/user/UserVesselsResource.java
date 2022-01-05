package pl.devcezz.barentswatch.user;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import pl.devcezz.barentswatch.Registry;
import pl.devcezz.barentswatch.externalapi.BarentsWatchExternalApi;
import pl.devcezz.barentswatch.externalapi.OpenPosition;
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
    @RestClient
    BarentsWatchExternalApi barentsWatchExternalApi;

    @Inject
    JsonWebToken token;

    @POST
    @Path("/track")
    @RolesAllowed({ "user" })
    public void addVesselToTrack(Integer mmsi) {
        UserVessel user = userVesselRepository.find("email", token.getSubject()).firstResult();
        user.trackVessel(mmsi);

        OpenPosition openPosition = barentsWatchExternalApi.getVesselPositionFor(Registry.accessToken.get(), mmsi);
        user.addPointForVessel(
                openPosition.mmsi(),
                openPosition.createPointRegistry());

        userVesselRepository.update(user);
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