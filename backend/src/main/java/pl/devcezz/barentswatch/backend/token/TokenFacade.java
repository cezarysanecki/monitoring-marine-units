package pl.devcezz.barentswatch.backend.token;

import io.smallrye.jwt.build.Jwt;
import pl.devcezz.barentswatch.backend.authentication.UserCredentials;
import pl.devcezz.barentswatch.backend.authentication.UserService;
import pl.devcezz.barentswatch.backend.common.UserInfo;
import pl.devcezz.barentswatch.backend.token.exceptions.RefreshTokenExpiredException;
import pl.devcezz.barentswatch.backend.token.repositories.RefreshTokenEntity;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class TokenFacade {

    @Inject
    UserService userService;

    @Inject
    RefreshTokenService refreshTokenService;

    public UserTokens generateTokenFor(UserCredentials userCredentials) {
        UserInfo user = userService.findUserByEmail(userCredentials.email());
        return generateUserTokens(user);
    }

    public UserTokens generateApiTokenForRefreshToken(String refreshToken) {
        RefreshingProcessed refreshingProcessed = refreshTokenService.processRefreshing(refreshToken)
                .orElseThrow(RefreshTokenExpiredException::new);

        UserInfo userInfo = userService.findUserByEmail(refreshingProcessed.email());

        if (refreshingProcessed.isValid()) {
            return new UserTokens(generateApiToken(userInfo), refreshingProcessed.refreshToken());
        }

        return generateUserTokens(userInfo);
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
