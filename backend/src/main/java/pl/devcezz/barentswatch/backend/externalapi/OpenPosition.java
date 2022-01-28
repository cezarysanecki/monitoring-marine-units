package pl.devcezz.barentswatch.backend.externalapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import pl.devcezz.barentswatch.backend.common.Coordinates;
import pl.devcezz.barentswatch.backend.common.CurrentVesselRegistry;
import pl.devcezz.barentswatch.backend.common.PointInTime;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenPosition(String timeStamp, String name, Integer shipType, String destination, Integer mmsi,
                           Geometry geometry) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Geometry(String type, List<Double> coordinates) {
    }

    CurrentVesselRegistry toVesselRegistry() {
        return new CurrentVesselRegistry(mmsi, name, shipType, destination,
                new PointInTime(parseTimestamp(), new Coordinates(parseLatitude(), parseLongitude())));
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