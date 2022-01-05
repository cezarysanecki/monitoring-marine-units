package pl.devcezz.barentswatch.user;

import io.quarkus.mongodb.panache.PanacheQuery;
import io.quarkus.scheduler.Scheduled;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import pl.devcezz.barentswatch.Registry;
import pl.devcezz.barentswatch.externalapi.BarentsWatchExternalApi;
import pl.devcezz.barentswatch.externalapi.OpenPosition;
import pl.devcezz.barentswatch.user.entity.Point;
import pl.devcezz.barentswatch.user.entity.Track;
import pl.devcezz.barentswatch.user.entity.UserVessel;
import pl.devcezz.barentswatch.user.entity.Vessel;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class VesselTrackerScheduler {

    @Inject
    UserVesselRepository userVesselRepository;

    @Inject
    @RestClient
    BarentsWatchExternalApi barentsWatchExternalApi;

    @Scheduled(every = "10s")
    void fetchAccessToken() {
        PanacheQuery<UserVessel> users = userVesselRepository.find("vessels.status", "TRACKED");

        List<Integer> trackedMmsiList = users.stream()
                .flatMap(userVessel -> userVessel.vessels.stream())
                .filter(vessel -> vessel.status.equals(Vessel.Status.TRACKED))
                .map(vessel -> vessel.mmsi)
                .toList();

        Map<Integer, OpenPosition> mmsiOpenPositions = trackedMmsiList.stream()
                .map(mmsi -> barentsWatchExternalApi.getVesselPositionFor(Registry.accessToken.get(), mmsi))
                .collect(Collectors.toMap(OpenPosition::mmsi, openPosition -> openPosition));

        List<UserVessel> updatedUsers = users.stream().map(
                user -> {
                    user.vessels
                            .forEach(vessel -> {
                                if (mmsiOpenPositions.containsKey(vessel.mmsi)) {
                                    OpenPosition openPosition = mmsiOpenPositions.get(vessel.mmsi);
                                    Optional<Track> track = vessel.tracks.stream()
                                            .filter(t -> t.status.equals(Track.TrackStatus.OPENED))
                                            .findFirst();

                                    track.ifPresent(t -> {
                                        Optional<Point> p = t.points.stream()
                                                .max(Comparator.comparing(a -> a.timestamp));

                                        LocalDateTime openPositionTimestamp = LocalDateTime.parse(openPosition.timeStamp(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssVV"));
                                        p.ifPresentOrElse(p1 -> {
                                            if (openPositionTimestamp.isAfter(p1.timestamp)) {
                                                t.points.add(Point.createPoint(openPositionTimestamp, openPosition.fetchX(), openPosition.fetchY()));
                                            }
                                        }, () -> t.points.add(Point.createPoint(openPositionTimestamp, openPosition.fetchX(), openPosition.fetchY())));
                                    });
                                }
                            });
                    return user;
                }
        ).toList();

        userVesselRepository.update(updatedUsers);
    }
}
