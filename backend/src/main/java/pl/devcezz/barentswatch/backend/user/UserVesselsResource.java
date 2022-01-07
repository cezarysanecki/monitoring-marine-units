package pl.devcezz.barentswatch.backend.user;

import pl.devcezz.barentswatch.backend.Registry;
import pl.devcezz.barentswatch.backend.externalapi.BarentsWatchExternalApi;
import pl.devcezz.barentswatch.backend.externalapi.OpenPosition;
import pl.devcezz.barentswatch.backend.user.entity.UserVessel;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.rest.client.inject.RestClient;

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
        UserVessel user = userVesselRepository.find("email", token.getSubject()).firstResult();
        user.suspendTrackingVessel(mmsi);
        userVesselRepository.update(user);
    }

    @DELETE
    @Path("/track")
    @RolesAllowed({ "user" })
    public void removeTrackingVessel(Integer mmsi) {
        UserVessel user = userVesselRepository.find("email", token.getSubject()).firstResult();
        user.removeTrackingVessel(mmsi);
        userVesselRepository.update(user);
    }
}