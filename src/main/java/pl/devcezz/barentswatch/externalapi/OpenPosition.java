package pl.devcezz.barentswatch.externalapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import pl.devcezz.barentswatch.user.tracker.PointRegistry;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenPosition(String timeStamp, Integer mmsi, Geometry geometry) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    record Geometry(String type, List<Double> coordinates) {
    }

    public PointRegistry createPointRegistry() {
        return new PointRegistry(fetchTimestamp(), fetchX(), fetchY());
    }

    private LocalDateTime fetchTimestamp() {
        return LocalDateTime.parse(timeStamp, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssVV"));
    }

    private Double fetchX() {
        return geometry.coordinates.get(0);
    }

    private Double fetchY() {
        return geometry.coordinates.get(1);
    }
}
