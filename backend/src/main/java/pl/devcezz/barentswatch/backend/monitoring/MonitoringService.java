package pl.devcezz.barentswatch.backend.monitoring;

import pl.devcezz.barentswatch.backend.authentication.exceptions.UserNotFoundException;
import pl.devcezz.barentswatch.backend.common.Coordinates;
import pl.devcezz.barentswatch.backend.common.PointInTime;
import pl.devcezz.barentswatch.backend.common.Track;
import pl.devcezz.barentswatch.backend.common.UserMonitoring;
import pl.devcezz.barentswatch.backend.externalapi.BarentsWatchFacade;
import pl.devcezz.barentswatch.backend.monitoring.repositories.MonitoringRepository;
import pl.devcezz.barentswatch.backend.monitoring.repositories.MonitoringEntity;
import pl.devcezz.barentswatch.backend.monitoring.repositories.VesselEntity;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class MonitoringService {

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
        MonitoringEntity user = monitoringRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        user.trackVessel(mmsi);
        user.addPointForVessel(barentsWatchFacade.getVesselRegistryFor(mmsi));

        monitoringRepository.update(user);
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
}
