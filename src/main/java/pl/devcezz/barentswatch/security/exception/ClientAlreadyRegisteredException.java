package pl.devcezz.barentswatch.security.exception;

import pl.devcezz.barentswatch.BarentsWatchException;

public class ClientAlreadyRegisteredException extends BarentsWatchException {

    public ClientAlreadyRegisteredException(String email) {
        super(String.format("Client of that email already exists: %s", email));
    }
}
