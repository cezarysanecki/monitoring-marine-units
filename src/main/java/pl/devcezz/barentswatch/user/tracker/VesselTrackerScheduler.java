package pl.devcezz.barentswatch.user.tracker;

import io.quarkus.scheduler.Scheduled;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import pl.devcezz.barentswatch.Registry;
import pl.devcezz.barentswatch.externalapi.BarentsWatchExternalApi;
import pl.devcezz.barentswatch.externalapi.OpenPosition;
import pl.devcezz.barentswatch.user.UserVesselRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class VesselTrackerScheduler {

    @Inject
    UserVesselRepository userVesselRepository;

    @Inject
    @RestClient
    BarentsWatchExternalApi barentsWatchExternalApi;

    @Scheduled(every = "10s")
    void fetchAccessToken() {
        var users = userVesselRepository.find("vessels.status", "TRACKED")
                .stream().toList();

        List<Integer> trackedMmsi = users.stream()
                .flatMap(userVessel -> userVessel.trackedMmsi().stream())
                .toList();

        List<OpenPosition> mmsiOpenPositions = fetchPositionsFor(trackedMmsi);

        users.forEach(
                user -> mmsiOpenPositions.forEach(
                        openPosition -> user.addPointForVessel(
                                openPosition.mmsi(),
                                openPosition.createPointRegistry())
                )
        );

        userVesselRepository.update(users);
    }

    private List<OpenPosition> fetchPositionsFor(List<Integer> trackedMmsiList) {
        return trackedMmsiList.stream()
                .map(mmsi -> barentsWatchExternalApi.getVesselPositionFor(Registry.accessToken.get(), mmsi))
                .toList();
    }
}
