package pl.devcezz.barentswatch.user;

import pl.devcezz.barentswatch.BarentsWatchException;

public class VesselAlreadyTrackedException extends BarentsWatchException {

    public VesselAlreadyTrackedException() {
        super("Vessel is already tracked");
    }
}
