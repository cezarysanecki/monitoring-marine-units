package pl.devcezz.barentswatch.backend.monitoring.exceptions;

import pl.devcezz.barentswatch.backend.app.exceptions.ApplicationException;

public class VesselAlreadySuspendedException extends ApplicationException {

    public VesselAlreadySuspendedException() {
        super("Vessel is already suspended");
    }
}
