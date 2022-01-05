package pl.devcezz.barentswatch.user.entity;

import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.types.ObjectId;
import pl.devcezz.barentswatch.user.VesselAlreadySuspendedException;
import pl.devcezz.barentswatch.user.VesselIsNotTrackedException;
import pl.devcezz.barentswatch.user.tracker.PointRegistry;

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

    public void addPointForVessel(Integer mmsi, PointRegistry pointRegistry) {
        if (vessels.stream().noneMatch(vessel -> vessel.isTracking(mmsi))) {
            return;
        }

        vessels.stream()
                .filter(vessel -> vessel.isTracking(mmsi))
                .flatMap(vessel -> vessel.tracks.stream())
                .filter(Track::isOpened)
                .findFirst()
                .ifPresent(track -> track.addPoint(pointRegistry.timestamp(), pointRegistry.x(), pointRegistry.y()));
    }

    public List<Integer> trackedMmsi() {
        return vessels.stream()
                .map(vessel -> vessel.mmsi)
                .toList();
    }
}

