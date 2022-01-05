package pl.devcezz.barentswatch.user.exception;

import pl.devcezz.barentswatch.BarentsWatchException;

public class VesselAlreadyTrackedException extends BarentsWatchException {

    public VesselAlreadyTrackedException() {
        super("Vessel is already tracked");
    }
}
