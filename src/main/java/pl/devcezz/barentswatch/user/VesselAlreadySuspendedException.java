package pl.devcezz.barentswatch.user;

import pl.devcezz.barentswatch.BarentsWatchException;

public class VesselAlreadySuspendedException extends BarentsWatchException {

    public VesselAlreadySuspendedException() {
        super("Vessel is already suspended");
    }
}
