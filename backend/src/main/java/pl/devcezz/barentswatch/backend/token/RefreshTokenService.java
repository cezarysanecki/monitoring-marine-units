package pl.devcezz.barentswatch.backend.token;

import pl.devcezz.barentswatch.backend.token.exceptions.RefreshTokenExpiredException;
import pl.devcezz.barentswatch.backend.token.exceptions.RefreshTokenNotFoundException;
import pl.devcezz.barentswatch.backend.token.repositories.RefreshTokenEntity;
import pl.devcezz.barentswatch.backend.token.repositories.RefreshTokenRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Optional;

@ApplicationScoped
public class RefreshTokenService {

    @Inject
    RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public Optional<RefreshingProcessed> processRefreshing(String refreshToken) {
        RefreshTokenEntity entity = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(RefreshTokenNotFoundException::new);

        if (entity.isExpired()) {
            refreshTokenRepository.deleteByRefreshToken(refreshToken);
            return Optional.empty();
        }

        return Optional.of(new RefreshingProcessed(entity.isValid(), entity.email, entity.token));
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
}
