package pl.devcezz.barentswatch.backend.token;

import pl.devcezz.barentswatch.backend.app.exceptions.ApplicationException;

public class RefreshTokenNotFoundException extends ApplicationException {

    public RefreshTokenNotFoundException() {
        super("Refresh token not found");
    }
}
