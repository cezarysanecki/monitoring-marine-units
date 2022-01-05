package pl.devcezz.barentswatch.user;

import pl.devcezz.barentswatch.BarentsWatchException;

public class VesselAlreadySuspendedException extends BarentsWatchException {

    public VesselAlreadySuspendedException(String message) {
        super(message);
    }
}
