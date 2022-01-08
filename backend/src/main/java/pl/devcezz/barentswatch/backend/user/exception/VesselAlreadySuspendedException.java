package pl.devcezz.barentswatch.backend.user.exception;

import pl.devcezz.barentswatch.backend.app.BarentsWatchException;

public class VesselAlreadySuspendedException extends BarentsWatchException {

    public VesselAlreadySuspendedException() {
        super("Vessel is already suspended");
    }
}
