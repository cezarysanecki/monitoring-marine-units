package pl.devcezz.barentswatch.backend.common;

import java.util.List;

public record UserMonitoring(Integer mmsi, List<Track> tracks) {
}
