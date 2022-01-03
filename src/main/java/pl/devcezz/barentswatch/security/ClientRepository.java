package pl.devcezz.barentswatch.security;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ClientRepository implements PanacheRepository<Client> {

    public void add(String email, String password, String role) {
        Client client = new Client();
        client.email = email;
        client.password = BcryptUtil.bcryptHash(password);
        client.role = role;
        persist(client);
    }
}
