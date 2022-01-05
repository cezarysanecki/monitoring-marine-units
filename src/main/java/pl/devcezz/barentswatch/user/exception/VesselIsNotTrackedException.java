package pl.devcezz.barentswatch.user.exception;

import pl.devcezz.barentswatch.BarentsWatchException;

public class VesselIsNotTrackedException extends BarentsWatchException {

    public VesselIsNotTrackedException() {
        super("Vessel is not tracked");
    }
}
