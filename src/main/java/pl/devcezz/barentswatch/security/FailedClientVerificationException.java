package pl.devcezz.barentswatch.security;

public class FailedClientVerificationException extends RuntimeException {

    public FailedClientVerificationException(String message) {
        super(message);
    }
}
