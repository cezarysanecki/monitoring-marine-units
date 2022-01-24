package pl.devcezz.barentswatch.backend.token;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Optional;

@ApplicationScoped
public class RefreshTokenService {

    @Inject
    RefreshTokenRepository refreshTokenRepository;

    public Optional<RefreshTokenEntity> findByRefreshToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public void deleteByRefreshToken(String refreshToken) {
        refreshTokenRepository.deleteByRefreshToken(refreshToken);
    }

    @Transactional
    public String createRefreshTokenFor(String email) {
        RefreshTokenEntity refreshToken = RefreshTokenEntity.generateRefreshToken(email);

        refreshTokenRepository.findByEmail(email)
                .ifPresentOrElse(
                        entity -> {
                            entity.token = refreshToken.token;
                            entity.expiryDate = refreshToken.expiryDate;
                        },
                        () -> refreshTokenRepository.persist(refreshToken)
                );

        return refreshToken.token;
    }

    @Transactional
    public RefreshTokenEntity verifyExpiration(RefreshTokenEntity refreshTokenEntity) {
        if (refreshTokenEntity.isExpired()) {
            refreshTokenRepository.deleteByRefreshToken(refreshTokenEntity.token);
            throw new RefreshTokenExpiredException();
        }

        return refreshTokenEntity;
    }
}
