package pl.devcezz.barentswatch.backend.user.entity;

import pl.devcezz.barentswatch.backend.common.VesselRegistry;
import pl.devcezz.barentswatch.backend.user.exception.VesselAlreadyTrackedException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Vessel {

    public Integer mmsi;
    public Status status;
    public List<Track> tracks = new ArrayList<>();

    static Vessel createNewVessel(Integer mmsi) {
        Vessel vessel = new Vessel();
        vessel.mmsi = mmsi;
        vessel.status = Status.TRACKED;
        vessel.tracks.add(Track.createOpenedTrack());
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
        tracks.add(Track.createOpenedTrack());
    }

    void suspendTracking() {
        status = Vessel.Status.SUSPENDED;
        tracks.removeIf(Track::hasNoPoints);
        tracks.forEach(Track::close);
    }

    void addPoint(VesselRegistry vesselRegistry) {
        if (cannotAddPoint(vesselRegistry.timestamp())) {
            return;
        }

        tracks.stream()
                .filter(Track::isOpened)
                .findFirst()
                .ifPresent(track -> track.addPoint(vesselRegistry.timestamp(), vesselRegistry.point().x(), vesselRegistry.point().y()));
    }

    private boolean cannotAddPoint(LocalDateTime timestamp) {
        Optional<LocalDateTime> lastUpdate = tracks.stream()
                .map(Track::getLastUpdate)
                .flatMap(Optional::stream)
                .max(LocalDateTime::compareTo);

        return lastUpdate.map(lastTimestamp -> !lastTimestamp.isBefore(timestamp))
                .orElse(false);
    }

    enum Status {
        TRACKED, SUSPENDED
    }
}
