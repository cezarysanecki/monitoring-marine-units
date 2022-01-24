package pl.devcezz.barentswatch.backend.monitoring.exceptions;

import pl.devcezz.barentswatch.backend.app.exceptions.ApplicationException;

public class VesselAlreadyTrackedException extends ApplicationException {

    public VesselAlreadyTrackedException() {
        super("Vessel is already tracked");
    }
}
