package pl.devcezz.barentswatch.backend.authentication.verification;

public class FailedUserVerificationException extends RuntimeException {

    public FailedUserVerificationException(String message) {
        super(message);
    }
}
