package pl.devcezz.barentswatch.backend.security;

public class FailedUserVerificationException extends RuntimeException {

    public FailedUserVerificationException(String message) {
        super(message);
    }
}
