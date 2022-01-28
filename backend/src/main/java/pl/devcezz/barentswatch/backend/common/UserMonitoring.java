package pl.devcezz.barentswatch.backend.common;

import java.util.List;

public record UserMonitoring(Integer mmsi, String name, String shipType, boolean isSuspended, List<Track> tracks) {
}
