package pl.devcezz.barentswatch.backend.user.entity;

import java.time.LocalDateTime;

public class Point {

    public Double x;
    public Double y;
    public LocalDateTime timestamp;

    static Point createPoint(LocalDateTime timestamp, Double x, Double y) {
        Point point = new Point();
        point.timestamp = timestamp;
        point.x = x;
        point.y = y;
        return point;
    }
}
