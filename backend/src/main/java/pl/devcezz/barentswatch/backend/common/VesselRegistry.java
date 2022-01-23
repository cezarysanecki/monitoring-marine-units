package pl.devcezz.barentswatch.backend.common;

public record VesselRegistry(String timestamp, Integer mmsi, Coordinates coordinates) {

    public VesselRegistry(String timestamp, Integer mmsi, Double latitude, Double longitude) {
        this(timestamp, mmsi, new Coordinates(latitude, longitude));
    }
}
