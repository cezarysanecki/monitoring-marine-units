package pl.devcezz.barentswatch.backend.common;

import java.time.ZonedDateTime;

public record VesselRegistry(ZonedDateTime timestamp, Integer mmsi, Coordinates coordinates) {

    public VesselRegistry(ZonedDateTime timestamp, Integer mmsi, Double latitude, Double longitude) {
        this(timestamp, mmsi, new Coordinates(latitude, longitude));
    }
}
