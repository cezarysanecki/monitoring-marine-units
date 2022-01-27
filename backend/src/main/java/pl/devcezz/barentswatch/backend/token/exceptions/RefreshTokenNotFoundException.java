package pl.devcezz.barentswatch.backend.token.exceptions;

public class RefreshTokenNotFoundException extends RefreshTokenException {

    public RefreshTokenNotFoundException() {
        super("Refresh token not found");
    }
}
