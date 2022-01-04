package pl.devcezz.barentswatch.user.entity;

import java.util.List;

public class Vessel {
    public Integer mmsi;
    public Status status;
    public List<Track> tracks;

    public static Vessel createNewVessel(Integer mmsi) {
        Vessel vessel = new Vessel();
        vessel.mmsi = mmsi;
        vessel.status = Status.TRACKED;
        return vessel;
    }

    public enum Status {
        TRACKED, SUSPENDED
    }
}
