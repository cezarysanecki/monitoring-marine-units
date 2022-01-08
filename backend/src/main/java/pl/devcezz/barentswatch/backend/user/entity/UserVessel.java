package pl.devcezz.barentswatch.backend.user.entity;

import pl.devcezz.barentswatch.backend.user.exception.VesselAlreadySuspendedException;
import pl.devcezz.barentswatch.backend.user.exception.VesselIsNotTrackedException;
import pl.devcezz.barentswatch.backend.common.VesselRegistry;
import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@MongoEntity(collection = "userVessel")
public class UserVessel {

    public ObjectId id;
    public String email;
    public List<Vessel> vessels = new ArrayList<>();

    public static UserVessel createUser(String email) {
        UserVessel userVessel = new UserVessel();
        userVessel.email = email;
        return userVessel;
    }

    public void trackVessel(Integer mmsi) {
        Optional<Vessel> containedVessel = vessels.stream()
                .filter(vessel -> vessel.isFor(mmsi))
                .findFirst();

        containedVessel.ifPresentOrElse(
                Vessel::resumeTracking,
                () -> vessels.add(Vessel.createNewVessel(mmsi))
        );
    }

    public void suspendTrackingVessel(Integer mmsi) {
        if (vessels.stream().anyMatch(vessel -> vessel.isSuspended(mmsi))) {
            throw new VesselAlreadySuspendedException();
        }

        vessels.stream()
                .filter(vessel -> vessel.isTracking(mmsi))
                .findFirst()
                .ifPresent(Vessel::suspendTracking);
    }

    public void removeTrackingVessel(Integer mmsi) {
        if (vessels.stream().noneMatch(vessel -> vessel.isFor(mmsi))) {
            throw new VesselIsNotTrackedException();
        }
        vessels.removeIf(vessel -> vessel.isFor(mmsi));
    }

    public void addPointForVessel(VesselRegistry vesselRegistry) {
        if (vessels.stream().noneMatch(vessel -> vessel.isTracking(vesselRegistry.mmsi()))) {
            return;
        }

        vessels.stream()
                .filter(vessel -> vessel.isTracking(vesselRegistry.mmsi()))
                .findFirst()
                .ifPresent(vessel -> vessel.addPoint(vesselRegistry));
    }

    public List<Integer> trackedMmsi() {
        return vessels.stream()
                .map(vessel -> vessel.mmsi)
                .toList();
    }
}

