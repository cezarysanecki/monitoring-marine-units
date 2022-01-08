package pl.devcezz.barentswatch.backend.common;

import java.time.LocalDateTime;

public record VesselRegistry(LocalDateTime timestamp, Integer mmsi, Point point) {

    public VesselRegistry(LocalDateTime timestamp, Integer mmsi, Double x, Double y) {
        this(timestamp, mmsi, new Point(x, y));
    }
}
