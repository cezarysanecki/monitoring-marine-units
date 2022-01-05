package pl.devcezz.barentswatch.backend.user.tracker;

import java.time.LocalDateTime;

public record PointRegistry(LocalDateTime timestamp, Double x, Double y) {}
