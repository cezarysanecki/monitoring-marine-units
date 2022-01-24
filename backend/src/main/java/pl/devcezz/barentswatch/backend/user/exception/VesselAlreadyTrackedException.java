package pl.devcezz.barentswatch.backend.user.exception;

import pl.devcezz.barentswatch.backend.app.exceptions.ApplicationException;

public class VesselAlreadyTrackedException extends ApplicationException {

    public VesselAlreadyTrackedException() {
        super("Vessel is already tracked");
    }
}
