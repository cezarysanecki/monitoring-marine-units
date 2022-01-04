package pl.devcezz.barentswatch.user.entity;

import java.util.List;

public class Vessel {
    public Integer mmsi;
    public Status status;
    public List<Track> tracks;

    public enum Status {
        TRACKED, SUSPENDED
    }
}
