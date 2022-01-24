package pl.devcezz.barentswatch.backend;

import io.quarkus.arc.Priority;
import io.quarkus.runtime.StartupEvent;
import pl.devcezz.barentswatch.backend.authentication.repositories.UserRepository;
import pl.devcezz.barentswatch.backend.monitoring.repositories.MonitoringRepository;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;

@Singleton
public class Startup {

    @Inject
    UserRepository userRepository;

    @Inject
    MonitoringRepository monitoringRepository;

    @Priority(value = 100)
    public void deleteUserVessels(@Observes StartupEvent evt) {
        monitoringRepository.deleteAll();
    }

    @Priority(value = 101)
    @Transactional
    public void loadUsers(@Observes StartupEvent evt) {
        userRepository.deleteAll();

        userRepository.save("user@user.pl", "user", "user");
    }
}
