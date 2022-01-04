package pl.devcezz.barentswatch.user;

import io.quarkus.vertx.ConsumeEvent;
import pl.devcezz.barentswatch.security.ClientAddedEvent;
import pl.devcezz.barentswatch.user.entity.UserVessel;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class VesselService {

    @Inject
    UserVesselRepository userVesselRepository;

    @ConsumeEvent(value = "new-client")
    public void addNewClient(ClientAddedEvent event) {
        UserVessel userVessel = new UserVessel();
        userVessel.email = event.email();

        userVesselRepository.persist(userVessel);
    }
}

