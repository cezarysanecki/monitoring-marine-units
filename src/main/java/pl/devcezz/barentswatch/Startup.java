package pl.devcezz.barentswatch;

import io.quarkus.runtime.StartupEvent;
import pl.devcezz.barentswatch.security.Client;
import pl.devcezz.barentswatch.security.ClientRepository;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;

@Singleton
public class Startup {

    @Inject
    ClientRepository clientRepository;

    @Transactional
    public void loadUsers(@Observes StartupEvent evt) {
        clientRepository.deleteAll();

        clientRepository.add("admin", "admin", "admin");
        clientRepository.add("user", "user", "user");
    }
}
