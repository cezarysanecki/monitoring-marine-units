package pl.devcezz.barentswatch.backend.monitoring.repositories;

import pl.devcezz.barentswatch.backend.common.Coordinates;
import pl.devcezz.barentswatch.backend.common.CurrentVesselRegistry;
import pl.devcezz.barentswatch.backend.common.PointInTime;
import pl.devcezz.barentswatch.backend.common.Track;
import pl.devcezz.barentswatch.backend.common.UserMonitoring;
import pl.devcezz.barentswatch.backend.common.VesselToTrack;
import pl.devcezz.barentswatch.backend.monitoring.exceptions.VesselAlreadyTrackedException;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VesselEntity {

    public Integer mmsi;
    public String name;
    public String shipType;
    public Status status;
    public List<TrackEntity> tracks = new ArrayList<>();

    static VesselEntity createNewVessel(VesselToTrack vesselToTrack) {
        VesselEntity vessel = new VesselEntity();
        vessel.mmsi = vesselToTrack.mmsi();
        vessel.name = vesselToTrack.name();
        vessel.shipType = vesselToTrack.shipType();
        vessel.status = Status.TRACKED;
        vessel.tracks.add(TrackEntity.createOpenedTrack());
        return vessel;
    }

    boolean isFor(Integer mmsi) {
        return this.mmsi.equals(mmsi);
    }

    boolean isTracking(Integer mmsi) {
        return this.status.equals(Status.TRACKED) && this.mmsi.equals(mmsi);
    }

    boolean isSuspended(Integer mmsi) {
        return this.status.equals(Status.SUSPENDED) && this.mmsi.equals(mmsi);
    }

    void resumeTracking() {
        if (status.equals(Status.TRACKED)) {
            throw new VesselAlreadyTrackedException();
        }

        status = Status.TRACKED;
        tracks.add(TrackEntity.createOpenedTrack());
    }

    void suspendTracking() {
        status = VesselEntity.Status.SUSPENDED;
        tracks.removeIf(TrackEntity::hasNoPoints);
        tracks.forEach(TrackEntity::close);
    }

    void addPoint(CurrentVesselRegistry currentVesselRegistry) {
        if (cannotAddPoint(currentVesselRegistry.pointInTime().timestamp())) {
            return;
        }

        tracks.stream()
                .filter(TrackEntity::isOpened)
                .findFirst()
                .ifPresent(track -> track.addPoint(
                        currentVesselRegistry.pointInTime().timestamp(),
                        currentVesselRegistry.pointInTime().coordinates().latitude(),
                        currentVesselRegistry.pointInTime().coordinates().longitude()));
    }

    UserMonitoring convertToUserMonitoring() {
        return new UserMonitoring(
                mmsi,
                name,
                shipType,
                status.equals(Status.SUSPENDED),
                tracks.stream()
                        .map(track -> track.coordinates.stream()
                                .map(coordinates -> new PointInTime(coordinates.timestamp,
                                        new Coordinates(coordinates.latitude, coordinates.longitude))).toList())
                        .map(Track::new)
                        .toList()
        );
    }

    private boolean cannotAddPoint(String timestamp) {
        Optional<ZonedDateTime> lastUpdate = tracks.stream()
                .map(TrackEntity::getLastUpdate)
                .flatMap(Optional::stream)
                .max(ZonedDateTime::compareTo);

        return lastUpdate.map(lastTimestamp -> !lastTimestamp.isBefore(ZonedDateTime.parse(timestamp)))
                .orElse(false);
    }

    enum Status {
        TRACKED, SUSPENDED
    }
}
