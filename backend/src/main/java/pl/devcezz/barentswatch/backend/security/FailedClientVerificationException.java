package pl.devcezz.barentswatch.backend.security;

public class FailedClientVerificationException extends RuntimeException {

    public FailedClientVerificationException(String message) {
        super(message);
    }
}
