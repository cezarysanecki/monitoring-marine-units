package pl.devcezz.barentswatch.backend.authentication;

import pl.devcezz.barentswatch.backend.authentication.repositories.UserRepository;
import pl.devcezz.barentswatch.backend.common.UserInfo;
import pl.devcezz.barentswatch.backend.security.exception.UserAlreadyRegisteredException;
import pl.devcezz.barentswatch.backend.token.TokenFacade;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class UserService {

    @Inject
    UserRepository userRepository;

    @Inject
    TokenFacade tokenFacade;

    public Optional<UserInfo> findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(entity -> new UserInfo(entity.email, entity.role));
    }

    public void registerUser(UserCredentials credentials) {
        userRepository.findByEmail(credentials.email()).ifPresentOrElse(
                client -> {
                    throw new UserAlreadyRegisteredException(credentials.email());
                },
                () -> userRepository.save(credentials.email(), credentials.password(), "user")
        );
    }
}
