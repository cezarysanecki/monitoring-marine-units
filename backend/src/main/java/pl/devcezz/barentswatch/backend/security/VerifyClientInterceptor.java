package pl.devcezz.barentswatch.backend.security;

import io.quarkus.elytron.security.common.BcryptUtil;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@VerifyClient
@Interceptor
public class VerifyClientInterceptor {

    @Inject
    ClientRepository clientRepository;

    @AroundInvoke
    Object verifyClientInvocation(InvocationContext context) throws Exception {
        LoginRequest request = (LoginRequest) context.getParameters()[0];
        Client client = clientRepository.find("email", request.email()).firstResult();

        if (client == null || !BcryptUtil.matches(request.password(), client.password)) {
            throw new FailedClientVerificationException("invalid_client");
        }

        return context.proceed();
    }
}