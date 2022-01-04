package pl.devcezz.barentswatch.user.entity;

import java.util.List;

public class Track {
    public TrackStatus status;
    public List<Point> points;

    public enum TrackStatus {
        OPENED, CLOSED
    }
}
