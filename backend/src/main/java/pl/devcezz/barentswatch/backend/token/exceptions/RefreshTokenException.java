package pl.devcezz.barentswatch.backend.token.exceptions;

import pl.devcezz.barentswatch.backend.app.exceptions.ApplicationException;

public abstract class RefreshTokenException extends ApplicationException {
    public RefreshTokenException(String message) {
        super(message);
    }
}
