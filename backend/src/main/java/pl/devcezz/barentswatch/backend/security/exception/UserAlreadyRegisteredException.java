package pl.devcezz.barentswatch.backend.security.exception;

import pl.devcezz.barentswatch.backend.app.exceptions.ApplicationException;

public class UserAlreadyRegisteredException extends ApplicationException {

    public UserAlreadyRegisteredException(String email) {
        super(String.format("User of that email already exists: %s", email));
    }
}
