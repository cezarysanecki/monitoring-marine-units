package pl.devcezz.barentswatch.user;

import pl.devcezz.barentswatch.BarentsWatchException;

public class VesselIsNotTrackedException extends BarentsWatchException {

    public VesselIsNotTrackedException() {
        super("Vessel is not tracked");
    }
}
