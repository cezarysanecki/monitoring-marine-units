package pl.devcezz.barentswatch.backend.externalapi.exceptions;

import pl.devcezz.barentswatch.backend.app.exceptions.ApplicationException;

public class OpenPositionNotFoundException extends ApplicationException {

    public OpenPositionNotFoundException(Integer mmsi) {
        super("Open position not found for: " + mmsi);
    }
}
