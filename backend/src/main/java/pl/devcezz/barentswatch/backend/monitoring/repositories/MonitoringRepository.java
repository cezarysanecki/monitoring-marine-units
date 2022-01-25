package pl.devcezz.barentswatch.backend.monitoring.repositories;

import io.quarkus.mongodb.panache.PanacheMongoRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class MonitoringRepository implements PanacheMongoRepository<MonitoringEntity> {

    public Optional<MonitoringEntity> findByEmail(String email) {
        return find("email", email).firstResultOptional();
    }

    public List<MonitoringEntity> getTrackVessels() {
        return find("trackedVessels.status", "TRACKED").stream().toList();
    }
}