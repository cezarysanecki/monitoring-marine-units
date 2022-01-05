package pl.devcezz.barentswatch.backend.user.exception;

import pl.devcezz.barentswatch.backend.BarentsWatchException;

public class VesselIsNotTrackedException extends BarentsWatchException {

    public VesselIsNotTrackedException() {
        super("Vessel is not tracked");
    }
}
