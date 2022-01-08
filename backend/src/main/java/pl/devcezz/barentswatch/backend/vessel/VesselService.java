package pl.devcezz.barentswatch.backend.vessel;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import pl.devcezz.barentswatch.backend.Registry;
import pl.devcezz.barentswatch.backend.common.VesselRegistry;
import pl.devcezz.barentswatch.backend.externalapi.BarentsWatchExternalApi;
import pl.devcezz.barentswatch.backend.externalapi.OpenPosition;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class VesselService {

    @RestClient
    @Inject
    BarentsWatchExternalApi barentsWatchExternalApi;

    VesselRegistry fetchVesselPosition(Integer mmsi) {
        return barentsWatchExternalApi.getVesselPositionFor(Registry.accessToken.get(), mmsi)
                .createVesselRegistry();
    }

    List<VesselRegistry> fetchVesselsPositionsFor(Area area) {
        return barentsWatchExternalApi.getVesselsPositionsFor(
                Registry.accessToken.get(),
                area.xMin(),
                area.xMax(),
                area.yMin(),
                area.yMax()
        ).stream()
                .filter(OpenPosition::isPoint)
                .map(OpenPosition::createVesselRegistry)
                .collect(Collectors.toList());
    }
}

record Area(Double xMin, Double xMax, Double yMin, Double yMax) { }