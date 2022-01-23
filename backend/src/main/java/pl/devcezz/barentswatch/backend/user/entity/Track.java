package pl.devcezz.barentswatch.backend.user.entity;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class Track {

    public TrackStatus status;
    public List<Coordinates> coordinates;

    enum TrackStatus {
        OPENED, CLOSED
    }

    static Track createOpenedTrack() {
        Track track = new Track();
        track.status = TrackStatus.OPENED;
        return track;
    }

    Optional<ZonedDateTime> getLastUpdate() {
        if (coordinates == null) {
            return Optional.empty();
        }

        return coordinates.stream()
                .map(coordinates -> coordinates.timestamp)
                .map(ZonedDateTime::parse)
                .max(ZonedDateTime::compareTo);
    }

    boolean isOpened() {
        return status.equals(TrackStatus.OPENED);
    }

    void close() {
        status = TrackStatus.CLOSED;
    }

    boolean hasNoPoints() {
        return coordinates == null || coordinates.isEmpty();
    }

    void addPoint(String timestamp, Double latitude, Double longitude) {
        if (coordinates == null) {
            coordinates = new ArrayList<>();
        }

        Optional<Coordinates> point = coordinates.stream()
                .max(Comparator.comparing(p -> p.timestamp));

        point.ifPresentOrElse(p -> {
            if (ZonedDateTime.parse(timestamp).isAfter(ZonedDateTime.parse(p.timestamp))) {
                coordinates.add(Coordinates.createPoint(timestamp, latitude, longitude));
            }
        }, () -> coordinates.add(Coordinates.createPoint(timestamp, latitude, longitude)));
    }
}
