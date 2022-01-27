package pl.devcezz.barentswatch.backend.token.exceptions;

public class RefreshTokenExpiredException extends RefreshTokenException {

    public RefreshTokenExpiredException() {
        super("Refresh token expired");
    }
}
