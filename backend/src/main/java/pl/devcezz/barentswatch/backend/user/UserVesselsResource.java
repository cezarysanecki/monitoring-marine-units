package pl.devcezz.barentswatch.backend.user;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import pl.devcezz.barentswatch.backend.Registry;
import pl.devcezz.barentswatch.backend.externalapi.BarentsWatchExternalApi;
import pl.devcezz.barentswatch.backend.externalapi.OpenPosition;
import pl.devcezz.barentswatch.backend.user.entity.UserVessel;
import pl.devcezz.barentswatch.backend.user.entity.Vessel;

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

@Path("/barentswatch/user")
public class UserVesselsResource {

    @Inject
    UserVesselRepository userVesselRepository;

    @Inject
    @RestClient
    BarentsWatchExternalApi barentsWatchExternalApi;

    @Inject
    JsonWebToken token;

    @GET
    @Path("/vessels")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"user"})
    public List<UserVesselResponse> getClientVessels() {
        UserVessel user = userVesselRepository.find("email", token.getSubject()).firstResult();
        return user.vessels.stream()
                .map(this::convertToResponse)
                .toList();
    }

    @POST
    @Path("/track")
    @RolesAllowed({"user"})
    public void addVesselToTrack(Integer mmsi) {
        UserVessel user = userVesselRepository.find("email", token.getSubject()).firstResult();
        user.trackVessel(mmsi);

        OpenPosition openPosition = barentsWatchExternalApi.getVesselPositionFor(Registry.accessToken.get(), mmsi);
        user.addPointForVessel(openPosition.createVesselRegistry());

        userVesselRepository.update(user);
    }

    @DELETE
    @Path("/track/suspend")
    @RolesAllowed({"user"})
    public void suspendTrackingVessel(Integer mmsi) {
        UserVessel user = userVesselRepository.find("email", token.getSubject()).firstResult();
        user.suspendTrackingVessel(mmsi);
        userVesselRepository.update(user);
    }

    @DELETE
    @Path("/track")
    @RolesAllowed({"user"})
    public void removeTrackingVessel(Integer mmsi) {
        UserVessel user = userVesselRepository.find("email", token.getSubject()).firstResult();
        user.removeTrackingVessel(mmsi);
        userVesselRepository.update(user);
    }

    private UserVesselResponse convertToResponse(Vessel vessel) {
        return new UserVesselResponse(
                vessel.mmsi,
                vessel.tracks.stream()
                        .map(track -> track.coordinates.stream().map(coordinates -> new VesselCoordinatesResponse(coordinates.timestamp.toString(),
                                coordinates.latitude, coordinates.longitude)).toList())
                        .map(VesselTrackResponse::new)
                        .toList()
        );
    }
}

record UserVesselResponse(Integer mmsi, List<VesselTrackResponse> tracks) {}

record VesselTrackResponse(List<VesselCoordinatesResponse> coordinates) {}

record VesselCoordinatesResponse(String timestamp, Double latitude, Double longitude) {}