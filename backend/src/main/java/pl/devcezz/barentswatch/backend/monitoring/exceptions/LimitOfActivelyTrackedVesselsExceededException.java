package pl.devcezz.barentswatch.backend.monitoring.exceptions;

import pl.devcezz.barentswatch.backend.app.exceptions.ApplicationException;

public class LimitOfActivelyTrackedVesselsExceededException extends ApplicationException {

    public LimitOfActivelyTrackedVesselsExceededException(Integer limit) {
        super("Cannot actively track more then " + limit + " vessels");
    }
}
