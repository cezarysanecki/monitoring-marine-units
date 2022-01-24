package pl.devcezz.barentswatch.backend.authentication.repositories;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.vertx.mutiny.core.eventbus.EventBus;
import pl.devcezz.barentswatch.backend.common.Events;
import pl.devcezz.barentswatch.backend.security.UserAddedEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class UserRepository implements PanacheRepository<UserEntity> {

    @Inject
    EventBus eventBus;

    public Optional<UserEntity> findByEmail(String email) {
        return find("email", email).stream()
                .findFirst();
    }

    public void save(String email, String password, String role) {
        UserEntity userEntity = new UserEntity();
        userEntity.email = email;
        userEntity.password = BcryptUtil.bcryptHash(password);
        userEntity.role = role;
        persist(userEntity);

        eventBus.send(Events.NEW_USER, new UserAddedEvent(email));
    }
}