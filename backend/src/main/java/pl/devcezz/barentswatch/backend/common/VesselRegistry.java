package pl.devcezz.barentswatch.backend.common;

import java.time.LocalDateTime;

public record VesselRegistry(LocalDateTime timestamp, Integer mmsi, Coordinates coordinates) {

    public VesselRegistry(LocalDateTime timestamp, Integer mmsi, Double latitude, Double longitude) {
        this(timestamp, mmsi, new Coordinates(latitude, longitude));
    }
}
