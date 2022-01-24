package pl.devcezz.barentswatch.backend.authentication;

import pl.devcezz.barentswatch.backend.authentication.repositories.UserRepository;
import pl.devcezz.barentswatch.backend.common.UserInfo;
import pl.devcezz.barentswatch.backend.authentication.exceptions.UserAlreadyRegisteredException;
import pl.devcezz.barentswatch.backend.authentication.exceptions.UserNotFoundException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class UserService {

    @Inject
    UserRepository userRepository;

    public UserInfo findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(entity -> new UserInfo(entity.email, entity.role))
                .orElseThrow(() -> new UserNotFoundException(email));
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
