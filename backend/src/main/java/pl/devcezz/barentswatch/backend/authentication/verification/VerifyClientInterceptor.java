package pl.devcezz.barentswatch.backend.authentication.verification;

import io.quarkus.elytron.security.common.BcryptUtil;
import pl.devcezz.barentswatch.backend.authentication.UserCredentials;
import pl.devcezz.barentswatch.backend.authentication.repositories.UserRepository;
import pl.devcezz.barentswatch.backend.security.FailedUserVerificationException;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@VerifyClient
@Interceptor
public class VerifyClientInterceptor {

    @Inject
    UserRepository userRepository;

    @AroundInvoke
    Object verifyClientInvocation(InvocationContext context) throws Exception {
        UserCredentials request = (UserCredentials) context.getParameters()[0];

        userRepository.findByEmail(request.email())
                .filter(user -> BcryptUtil.matches(request.password(), user.password))
                .orElseThrow(() -> new FailedUserVerificationException("invalid_client"));

        return context.proceed();
    }
}