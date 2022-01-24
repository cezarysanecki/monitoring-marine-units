package pl.devcezz.barentswatch.backend.monitoring;

import pl.devcezz.barentswatch.backend.common.constants.Events;
import pl.devcezz.barentswatch.backend.common.events.UserAddedEvent;
import pl.devcezz.barentswatch.backend.monitoring.repositories.MonitoringEntity;
import io.quarkus.vertx.ConsumeEvent;
import pl.devcezz.barentswatch.backend.monitoring.repositories.MonitoringRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class MonitoringEventHandler {

    @Inject
    MonitoringRepository monitoringRepository;

    @ConsumeEvent(value = Events.NEW_USER)
    public void addNewUser(UserAddedEvent event) {
        MonitoringEntity monitoringEntity = MonitoringEntity.createUser(event.email());
        monitoringRepository.persist(monitoringEntity);
    }
}

