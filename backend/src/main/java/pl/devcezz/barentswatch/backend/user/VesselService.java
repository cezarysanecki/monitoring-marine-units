package pl.devcezz.barentswatch.backend.user;

import pl.devcezz.barentswatch.backend.security.ClientAddedEvent;
import pl.devcezz.barentswatch.backend.user.entity.UserVessel;
import io.quarkus.vertx.ConsumeEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class VesselService {

    @Inject
    UserVesselRepository userVesselRepository;

    @ConsumeEvent(value = "new-client")
    public void addNewClient(ClientAddedEvent event) {
        UserVessel userVessel = UserVessel.createUser(event.email());
        userVesselRepository.persist(userVessel);
    }
}

