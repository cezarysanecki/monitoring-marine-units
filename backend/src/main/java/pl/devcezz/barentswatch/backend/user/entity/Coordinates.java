package pl.devcezz.barentswatch.backend.user.entity;

public class Coordinates {

    public String timestamp;
    public Double latitude;
    public Double longitude;

    static Coordinates createPoint(String timestamp, Double latitude, Double longitude) {
        Coordinates coordinates = new Coordinates();
        coordinates.timestamp = timestamp;
        coordinates.latitude = latitude;
        coordinates.longitude = longitude;
        return coordinates;
    }
}
