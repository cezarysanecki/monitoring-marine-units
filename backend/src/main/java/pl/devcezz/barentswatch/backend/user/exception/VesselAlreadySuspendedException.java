package pl.devcezz.barentswatch.backend.user.exception;

import pl.devcezz.barentswatch.backend.app.exceptions.ApplicationException;

public class VesselAlreadySuspendedException extends ApplicationException {

    public VesselAlreadySuspendedException() {
        super("Vessel is already suspended");
    }
}
