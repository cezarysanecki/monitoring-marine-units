package pl.devcezz.barentswatch.backend.monitoring;

import pl.devcezz.barentswatch.backend.authentication.exceptions.UserNotFoundException;
import pl.devcezz.barentswatch.backend.common.UserMonitoring;
import pl.devcezz.barentswatch.backend.externalapi.BarentsWatchFacade;
import pl.devcezz.barentswatch.backend.monitoring.exceptions.LimitOfAllTrackedVesselsExceededException;
import pl.devcezz.barentswatch.backend.monitoring.exceptions.LimitOfActivelyTrackedVesselsExceededException;
import pl.devcezz.barentswatch.backend.monitoring.repositories.MonitoringRepository;
import pl.devcezz.barentswatch.backend.monitoring.repositories.MonitoringEntity;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class MonitoringService {

    private static final int LIMIT_OF_ACTIVELY_TRACKED_VESSELS = 10;
    private static final int LIMIT_OF_ALL_TRACKED_VESSELS = LIMIT_OF_ACTIVELY_TRACKED_VESSELS + 5;

    @Inject
    BarentsWatchFacade barentsWatchFacade;

    @Inject
    MonitoringRepository monitoringRepository;

    public List<UserMonitoring> getTackedVessels(String email) {
        MonitoringEntity entity = monitoringRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        return entity.convertToMonitoringList();
    }

    void trackVessel(String email, Integer mmsi) {
        MonitoringEntity entity = monitoringRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        validate(mmsi, entity);

        entity.trackVessel(mmsi);
        entity.addPointForVessel(barentsWatchFacade.getVesselRegistryFor(mmsi));

        monitoringRepository.update(entity);
    }

    void suspendTracking(String email, Integer mmsi) {
        MonitoringEntity user = monitoringRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        user.suspendTrackingVessel(mmsi);

        monitoringRepository.update(user);
    }

    void removeTracking(String email, Integer mmsi) {
        MonitoringEntity user = monitoringRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        user.removeTrackingVessel(mmsi);

        monitoringRepository.update(user);
    }

    private void validate(Integer mmsi, MonitoringEntity user) {
        if (user.isTracking(mmsi)) {
            Long numberOfActivelyTrackedVessels = user.numberOfTrackedVessels();
            if (numberOfActivelyTrackedVessels >= LIMIT_OF_ACTIVELY_TRACKED_VESSELS) {
                throw new LimitOfActivelyTrackedVesselsExceededException(LIMIT_OF_ACTIVELY_TRACKED_VESSELS);
            }
        } else {
            Long numberOfActivelyTrackedVessels = user.numberOfTrackedVessels();
            if (numberOfActivelyTrackedVessels >= LIMIT_OF_ACTIVELY_TRACKED_VESSELS) {
                throw new LimitOfActivelyTrackedVesselsExceededException(LIMIT_OF_ACTIVELY_TRACKED_VESSELS);
            }
            if (user.trackedVessels.size() >= LIMIT_OF_ALL_TRACKED_VESSELS) {
                throw new LimitOfAllTrackedVesselsExceededException(LIMIT_OF_ALL_TRACKED_VESSELS);
            }
        }
    }
}
