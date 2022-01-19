package pl.devcezz.barentswatch.backend.user.entity;

import java.time.ZonedDateTime;

public class Coordinates {

    public ZonedDateTime timestamp;
    public Double latitude;
    public Double longitude;

    static Coordinates createPoint(ZonedDateTime timestamp, Double latitude, Double longitude) {
        Coordinates coordinates = new Coordinates();
        coordinates.timestamp = timestamp;
        coordinates.latitude = latitude;
        coordinates.longitude = longitude;
        return coordinates;
    }
}
