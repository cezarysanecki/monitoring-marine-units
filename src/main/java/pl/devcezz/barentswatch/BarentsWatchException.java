package pl.devcezz.barentswatch;

public class BarentsWatchException extends RuntimeException {

    public BarentsWatchException(String message) {
        super(message);
    }

    public BarentsWatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
