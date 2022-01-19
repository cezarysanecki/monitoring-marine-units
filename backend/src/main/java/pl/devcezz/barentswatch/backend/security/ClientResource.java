package pl.devcezz.barentswatch.backend.security;

import io.smallrye.jwt.build.Jwt;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/barentswatch/client")
public class ClientResource {

    @Inject
    ClientRepository clientRepository;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/token")
    @VerifyClient
    public TokenResponse generateAccessToken(LoginRequest request) {
        Client client = clientRepository.find("email", request.email()).firstResult();

        String token = Jwt.subject(client.email)
                .groups(client.role)
                .sign();

        return new TokenResponse(token);
    }
}

record LoginRequest(String email, String password) {}

record TokenResponse(String token) {}