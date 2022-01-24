package pl.devcezz.barentswatch.backend.token;

import org.eclipse.microprofile.config.ConfigProvider;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Entity
@Table(name = "refresh_token")
public class RefreshTokenEntity {

    static Integer DURATION_SEC = ConfigProvider.getConfig()
            .getValue("barents-watch.jwt.refresh-token.duration-seconds", Integer.class);

    @Id
    @GeneratedValue
    public Long id;
    public String email;
    public String token;
    public Instant expiryDate;

    static RefreshTokenEntity generateRefreshToken(String email) {
        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.email = email;
        refreshTokenEntity.expiryDate = Instant.now().plus(DURATION_SEC, ChronoUnit.SECONDS);
        refreshTokenEntity.token = UUID.randomUUID().toString();
        return refreshTokenEntity;
    }

    boolean isValid() {
        return !isExpired();
    }

    boolean isExpired() {
        return Instant.now().isAfter(expiryDate);
    }
}
