package pl.devcezz.barentswatch.user.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Track {
    public TrackStatus status;
    public List<Point> points = new ArrayList<>();

    public enum TrackStatus {
        OPENED, CLOSED
    }

    public static Track createOpenedTrack() {
        Track track = new Track();
        track.status = TrackStatus.OPENED;
        return track;
    }
}
