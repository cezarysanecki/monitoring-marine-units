package pl.devcezz.barentswatch.backend.monitoring.exceptions;

import pl.devcezz.barentswatch.backend.app.exceptions.ApplicationException;

public class VesselIsNotTrackedException extends ApplicationException {

    public VesselIsNotTrackedException() {
        super("Vessel is not tracked");
    }
}
