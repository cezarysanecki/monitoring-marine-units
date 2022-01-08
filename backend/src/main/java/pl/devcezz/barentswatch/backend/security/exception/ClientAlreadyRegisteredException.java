package pl.devcezz.barentswatch.backend.security.exception;

import pl.devcezz.barentswatch.backend.app.BarentsWatchException;

public class ClientAlreadyRegisteredException extends BarentsWatchException {

    public ClientAlreadyRegisteredException(String email) {
        super(String.format("Client of that email already exists: %s", email));
    }
}
