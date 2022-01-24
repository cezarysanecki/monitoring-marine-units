package pl.devcezz.barentswatch.backend.monitoring.repositories;

public class CoordinatesEntity {

    public String timestamp;
    public Double latitude;
    public Double longitude;

    static CoordinatesEntity createPoint(String timestamp, Double latitude, Double longitude) {
        CoordinatesEntity coordinates = new CoordinatesEntity();
        coordinates.timestamp = timestamp;
        coordinates.latitude = latitude;
        coordinates.longitude = longitude;
        return coordinates;
    }
}
