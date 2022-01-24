package pl.devcezz.barentswatch.backend.monitoring.tracker;

import io.quarkus.scheduler.Scheduled;
import pl.devcezz.barentswatch.backend.common.CurrentVesselRegistry;
import pl.devcezz.barentswatch.backend.externalapi.BarentsWatchFacade;
import pl.devcezz.barentswatch.backend.monitoring.repositories.MonitoringRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class VesselTrackerScheduler {

    @Inject
    MonitoringRepository monitoringRepository;

    @Inject
    BarentsWatchFacade barentsWatchFacade;

    @Scheduled(every = "10s")
    void fetchAccessToken() {
        var users = monitoringRepository.getTrackVessels();

        List<Integer> trackedMmsi = users.stream()
                .flatMap(userVessel -> userVessel.trackedMmsi().stream())
                .toList();

        List<CurrentVesselRegistry> mmsiOpenPositions = fetchPositionsFor(trackedMmsi);

        users.forEach(
                user -> mmsiOpenPositions.forEach(user::addPointForVessel)
        );

        monitoringRepository.update(users);
    }

    private List<CurrentVesselRegistry> fetchPositionsFor(List<Integer> trackedMmsiList) {
        return trackedMmsiList.stream()
                .map(mmsi -> barentsWatchFacade.getVesselRegistryFor(mmsi))
                .toList();
    }
}
