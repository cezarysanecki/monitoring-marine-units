package pl.devcezz.barentswatch.externalapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenPosition(String timeStamp, Integer mmsi, Geometry geometry) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    record Geometry(String type, List<Double> coordinates) {
    }

    public Double fetchX() {
        return geometry.coordinates.get(0);
    }

    public Double fetchY() {
        return geometry.coordinates.get(1);
    }
}
