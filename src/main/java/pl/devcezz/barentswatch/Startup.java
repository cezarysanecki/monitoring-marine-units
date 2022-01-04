package pl.devcezz.barentswatch;

import io.quarkus.arc.Priority;
import io.quarkus.runtime.StartupEvent;
import pl.devcezz.barentswatch.security.ClientRepository;
import pl.devcezz.barentswatch.user.UserVesselRepository;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;

@Singleton
public class Startup {

    @Inject
    ClientRepository clientRepository;

    @Inject
    UserVesselRepository userVesselRepository;

    @Priority(value = 100)
    public void deleteUserVessels(@Observes StartupEvent evt) {
        userVesselRepository.deleteAll();
    }

    @Priority(value = 101)
    @Transactional
    public void loadUsers(@Observes StartupEvent evt) {
        clientRepository.deleteAll();

        clientRepository.add("user@user.pl", "user", "user");
    }
}
