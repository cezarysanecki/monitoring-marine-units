package pl.devcezz.barentswatch.backend.user.exception;

import pl.devcezz.barentswatch.backend.app.exceptions.ApplicationException;

public class VesselIsNotTrackedException extends ApplicationException {

    public VesselIsNotTrackedException() {
        super("Vessel is not tracked");
    }
}
