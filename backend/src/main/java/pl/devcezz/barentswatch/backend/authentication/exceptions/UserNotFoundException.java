package pl.devcezz.barentswatch.backend.authentication.exceptions;

import pl.devcezz.barentswatch.backend.app.exceptions.ApplicationException;

public class UserNotFoundException extends ApplicationException {

    public UserNotFoundException(String email) {
        super("User not found: " + email);
    }
}
