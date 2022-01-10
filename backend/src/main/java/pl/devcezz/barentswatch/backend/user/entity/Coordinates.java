package pl.devcezz.barentswatch.backend.user.entity;

import java.time.LocalDateTime;

public class Coordinates {

    public LocalDateTime timestamp;
    public Double latitude;
    public Double longitude;

    static Coordinates createPoint(LocalDateTime timestamp, Double latitude, Double longitude) {
        Coordinates coordinates = new Coordinates();
        coordinates.timestamp = timestamp;
        coordinates.latitude = latitude;
        coordinates.longitude = longitude;
        return coordinates;
    }
}
