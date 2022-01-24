package pl.devcezz.barentswatch.backend.externalapi;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import pl.devcezz.barentswatch.backend.Registry;
import pl.devcezz.barentswatch.backend.common.Area;
import pl.devcezz.barentswatch.backend.common.CurrentVesselRegistry;
import pl.devcezz.barentswatch.backend.externalapi.exceptions.OpenPositionNotFoundException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class BarentsWatchFacade {

    @RestClient
    @Inject
    BarentsWatchExternalApi barentsWatchExternalApi;

    public List<CurrentVesselRegistry> getVesselsRegistryFor(Area area) {
        return barentsWatchExternalApi.fetchOpenPositionsFor(Registry.accessToken.get(),
                        area.xMin(), area.xMax(), area.yMin(), area.yMax())
                .stream()
                .filter(OpenPosition::isPoint)
                .map(OpenPosition::toVesselRegistry)
                .toList();
    }

    public CurrentVesselRegistry getVesselRegistryFor(Integer mmsi) {
        return Optional.ofNullable(
                        barentsWatchExternalApi.fetchOpenPositionFor(Registry.accessToken.get(), mmsi))
                .filter(OpenPosition::isPoint)
                .map(OpenPosition::toVesselRegistry)
                .orElseThrow(() -> new OpenPositionNotFoundException(mmsi));
    }
}
