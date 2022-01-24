package pl.devcezz.barentswatch.backend.token.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class RefreshTokenRepository implements PanacheRepository<RefreshTokenEntity> {

    public Optional<RefreshTokenEntity> findByRefreshToken(String token) {
        return find("token", token).stream()
                .findFirst();
    }

    public Optional<RefreshTokenEntity> findByEmail(String email) {
        return find("email", email).stream()
                .findFirst();
    }

    public void deleteByRefreshToken(String token) {
        delete("token", token);
    }
}