package pl.devcezz.barentswatch.backend.externalapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import pl.devcezz.barentswatch.backend.common.VesselRegistry;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenPosition(String timeStamp, Integer mmsi, Geometry geometry) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Geometry(String type, List<Double> coordinates) { }

    public VesselRegistry createVesselRegistry() {
        return new VesselRegistry(fetchTimestamp(), mmsi, fetchX(), fetchY());
    }

    public boolean isPoint() {
        return geometry.type.equals("Point");
    }

    private LocalDateTime fetchTimestamp() {
        return LocalDateTime.parse(timeStamp, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssVV"));
    }

    private Double fetchX() {
        return geometry.coordinates().get(0);
    }

    private Double fetchY() {
        return geometry.coordinates().get(1);
    }
}