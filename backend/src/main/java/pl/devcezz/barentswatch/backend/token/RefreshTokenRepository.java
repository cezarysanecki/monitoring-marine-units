package pl.devcezz.barentswatch.backend.token;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class RefreshTokenRepository implements PanacheRepository<RefreshTokenEntity> {

    Optional<RefreshTokenEntity> findByToken(String token) {
        return find("token", token).stream()
                .findFirst();
    }

    Optional<RefreshTokenEntity> findByEmail(String email) {
        return find("email", email).stream()
                .findFirst();
    }

    void updateTokenAndExpiryDateForEmail(String email, String token, String expiryDate) {
        update("token = ?1, expiryDate = ?2 where email = ?3", token, expiryDate, email);
    }

    void deleteByRefreshToken(String token) {
        delete("token", token);
    }
}