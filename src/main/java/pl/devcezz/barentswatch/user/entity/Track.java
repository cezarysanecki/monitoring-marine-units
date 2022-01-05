package pl.devcezz.barentswatch.user.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class Track {

    public TrackStatus status;
    public List<Point> points;

    enum TrackStatus {
        OPENED, CLOSED
    }

    static Track createOpenedTrack() {
        Track track = new Track();
        track.status = TrackStatus.OPENED;
        return track;
    }

    Optional<LocalDateTime> getLastUpdate() {
        if (points == null) {
            return Optional.empty();
        }

        return points.stream()
                .map(point -> point.timestamp)
                .max(LocalDateTime::compareTo);
    }

    boolean isOpened() {
        return status.equals(TrackStatus.OPENED);
    }

    void close() {
        status = TrackStatus.CLOSED;
    }

    boolean hasNoPoints() {
        return points == null || points.isEmpty();
    }

    void addPoint(LocalDateTime timestamp, Double x, Double y) {
        if (points == null) {
            points = new ArrayList<>();
        }

        Optional<Point> point = points.stream()
                .max(Comparator.comparing(p -> p.timestamp));

        point.ifPresentOrElse(p1 -> {
            if (timestamp.isAfter(p1.timestamp)) {
                points.add(Point.createPoint(timestamp, x, y));
            }
        }, () -> points.add(Point.createPoint(timestamp, x, y)));
    }
}
