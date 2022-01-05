package pl.devcezz.barentswatch.user.entity;

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

    boolean isTracking(Integer mmsi) {
        return this.status.equals(Vessel.Status.TRACKED) && this.mmsi.equals(mmsi);
    }

    enum Status {
        TRACKED, SUSPENDED
    }
}
