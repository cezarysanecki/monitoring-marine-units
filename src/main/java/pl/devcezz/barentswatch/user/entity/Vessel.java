package pl.devcezz.barentswatch.user.entity;

import pl.devcezz.barentswatch.user.VesselAlreadyTrackedException;

import java.util.ArrayList;
import java.util.List;

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
        tracks.forEach(Track::close);
    }

    enum Status {
        TRACKED, SUSPENDED
    }
}
