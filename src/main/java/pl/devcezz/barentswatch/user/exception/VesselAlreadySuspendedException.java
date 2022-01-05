package pl.devcezz.barentswatch.user.exception;

import pl.devcezz.barentswatch.BarentsWatchException;

public class VesselAlreadySuspendedException extends BarentsWatchException {

    public VesselAlreadySuspendedException() {
        super("Vessel is already suspended");
    }
}
