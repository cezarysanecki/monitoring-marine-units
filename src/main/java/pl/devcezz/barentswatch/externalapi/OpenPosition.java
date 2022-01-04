package pl.devcezz.barentswatch.externalapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenPosition(String timeStamp, Integer mmsi, Geometry geometry) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    record Geometry(String type, List<Double> coordinates) {
    }
}
