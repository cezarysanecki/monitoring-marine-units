package pl.devcezz.barentswatch.security;

import io.smallrye.jwt.build.Jwt;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.time.Duration;

@Path("/barentswatch/client")
public class ClientResource {

    @Inject
    ClientRepository clientRepository;

    @ConfigProperty(name = "smallrye.jwt.verify.issuer")
    public String issuer;

    @ConfigProperty(name = "smallrye.jwt.verify.expired-at.minutes")
    public Integer expiredAtInMinutes;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/token")
    @VerifyClient
    public TokenResponse generateAccessToken(LoginRequest request) {
        Client client = clientRepository.find("email", request.email()).firstResult();

        String token = Jwt.issuer(issuer)
                .subject(client.email)
                .expiresIn(Duration.ofMinutes(expiredAtInMinutes))
                .groups(client.role)
                .sign();

        return new TokenResponse(token, expiredAtInMinutes * 60);
    }
}

record LoginRequest(String email, String password) {}

record TokenResponse(String apiToken, Integer expiresAt) {}