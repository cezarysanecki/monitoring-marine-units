package pl.devcezz.barentswatch.security;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.vertx.mutiny.core.eventbus.EventBus;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class ClientRepository implements PanacheRepository<Client> {

    @Inject
    EventBus eventBus;

    public void add(String email, String password, String role) {
        Client client = new Client();
        client.email = email;
        client.password = BcryptUtil.bcryptHash(password);
        client.role = role;
        persist(client);

        eventBus.send("new-client", new ClientAddedEvent(email));
    }
}