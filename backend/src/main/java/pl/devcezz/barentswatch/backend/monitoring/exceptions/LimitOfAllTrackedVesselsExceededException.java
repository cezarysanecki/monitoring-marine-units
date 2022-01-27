package pl.devcezz.barentswatch.backend.monitoring.exceptions;

import pl.devcezz.barentswatch.backend.app.exceptions.ApplicationException;

public class LimitOfAllTrackedVesselsExceededException extends ApplicationException {

    public LimitOfAllTrackedVesselsExceededException(Integer limit) {
        super("Cannot track more then " + limit + " vessels");
    }
}
