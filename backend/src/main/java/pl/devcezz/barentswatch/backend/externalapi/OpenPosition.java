package pl.devcezz.barentswatch.backend.externalapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import pl.devcezz.barentswatch.backend.common.Coordinates;
import pl.devcezz.barentswatch.backend.common.VesselRegistry;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenPosition(String timeStamp, Integer mmsi, Geometry geometry) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Geometry(String type, List<Double> coordinates) { }

    VesselRegistry toVesselRegistry() {
        return new VesselRegistry(parseTimestamp(), mmsi, new Coordinates(parseLatitude(), parseLongitude()));
    }

    boolean isPoint() {
        return geometry.type.equals("Point");
    }

    private String parseTimestamp() {
        return ZonedDateTime.parse(timeStamp, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssVV"))
                .toString();
    }

    private Double parseLatitude() {
        return geometry.coordinates().get(1);
    }

    private Double parseLongitude() {
        return geometry.coordinates().get(0);
    }
}