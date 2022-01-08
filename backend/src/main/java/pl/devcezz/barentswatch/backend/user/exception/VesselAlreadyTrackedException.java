package pl.devcezz.barentswatch.backend.user.exception;

import pl.devcezz.barentswatch.backend.app.BarentsWatchException;

public class VesselAlreadyTrackedException extends BarentsWatchException {

    public VesselAlreadyTrackedException() {
        super("Vessel is already tracked");
    }
}
