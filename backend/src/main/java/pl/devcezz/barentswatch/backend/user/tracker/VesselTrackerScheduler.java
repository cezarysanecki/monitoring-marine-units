package pl.devcezz.barentswatch.backend.user.tracker;

import io.quarkus.scheduler.Scheduled;
import pl.devcezz.barentswatch.backend.common.VesselRegistry;
import pl.devcezz.barentswatch.backend.externalapi.BarentsWatchFacade;
import pl.devcezz.barentswatch.backend.user.UserVesselRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class VesselTrackerScheduler {

    @Inject
    UserVesselRepository userVesselRepository;

    @Inject
    BarentsWatchFacade barentsWatchFacade;

    @Scheduled(every = "10s")
    void fetchAccessToken() {
        var users = userVesselRepository.find("vessels.status", "TRACKED")
                .stream().toList();

        List<Integer> trackedMmsi = users.stream()
                .flatMap(userVessel -> userVessel.trackedMmsi().stream())
                .toList();

        List<VesselRegistry> mmsiOpenPositions = fetchPositionsFor(trackedMmsi);

        users.forEach(
                user -> mmsiOpenPositions.forEach(user::addPointForVessel)
        );

        userVesselRepository.update(users);
    }

    private List<VesselRegistry> fetchPositionsFor(List<Integer> trackedMmsiList) {
        return trackedMmsiList.stream()
                .map(mmsi -> barentsWatchFacade.getVesselRegistryFor(mmsi))
                .toList();
    }
}
