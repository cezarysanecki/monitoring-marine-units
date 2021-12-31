package pl.devcezz.barentswatch.externalapi;

import java.util.List;

public record OpenPosition(String timeStamp, Integer mmsi, Geometry geometry) {

    record Geometry(String type, List<Double> coordinates) {
    }
}
