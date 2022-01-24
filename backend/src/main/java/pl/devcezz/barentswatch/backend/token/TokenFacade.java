package pl.devcezz.barentswatch.backend.token;

import io.smallrye.jwt.build.Jwt;
import pl.devcezz.barentswatch.backend.authentication.UserCredentials;
import pl.devcezz.barentswatch.backend.authentication.UserService;
import pl.devcezz.barentswatch.backend.common.UserInfo;
import pl.devcezz.barentswatch.backend.security.exception.UserNotFoundException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class TokenFacade {

    @Inject
    UserService userService;

    @Inject
    RefreshTokenService refreshTokenService;

    public UserTokens generateTokenFor(UserCredentials userCredentials) {
        return userService.findUserByEmail(userCredentials.email())
                .map(this::generateUserTokens)
                .orElseThrow(() -> new UserNotFoundException(userCredentials.email()));
    }

    public UserTokens generateApiTokenFor(String refreshToken) {
        RefreshTokenEntity entity = refreshTokenService.findByRefreshToken(refreshToken)
                .orElseThrow(RefreshTokenNotFoundException::new);

        if (entity.isExpired()) {
            refreshTokenService.deleteByRefreshToken(refreshToken);
            throw new RefreshTokenExpiredException();
        }

        return userService.findUserByEmail(entity.email)
                .map(this::generateUserTokens)
                .orElseThrow(() -> new UserNotFoundException(entity.email));
    }

    private UserTokens generateUserTokens(UserInfo userInfo) {
        String apiToken = generateApiToken(userInfo);
        String refreshToken = refreshTokenService.createRefreshTokenFor(userInfo.email());

        return new UserTokens(apiToken, refreshToken);
    }

    private String generateApiToken(UserInfo userInfo) {
        return Jwt.subject(userInfo.email())
                .groups(userInfo.role())
                .sign();
    }
}
