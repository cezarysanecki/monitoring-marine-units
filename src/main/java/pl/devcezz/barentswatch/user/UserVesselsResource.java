package pl.devcezz.barentswatch.user;

import org.eclipse.microprofile.jwt.JsonWebToken;
import pl.devcezz.barentswatch.user.entity.UserVessel;
import pl.devcezz.barentswatch.user.entity.Vessel;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
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

        if (userVessel.containsVessel(mmsi)) {
            throw new VesselAlreadyTrackedException("Vessel is already tracked");
        }

        userVessel.vessels.add(Vessel.createNewVessel(mmsi));
        userVesselRepository.update(userVessel);
    }
}