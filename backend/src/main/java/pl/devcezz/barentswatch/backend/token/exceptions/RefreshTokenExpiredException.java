package pl.devcezz.barentswatch.backend.token.exceptions;

import pl.devcezz.barentswatch.backend.app.exceptions.ApplicationException;

public class RefreshTokenExpiredException extends ApplicationException {

    public RefreshTokenExpiredException() {
        super("Refresh token expired");
    }
}
