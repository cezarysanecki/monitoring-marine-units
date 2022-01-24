package pl.devcezz.barentswatch.backend.authentication;

import pl.devcezz.barentswatch.backend.authentication.verification.VerifyClient;
import pl.devcezz.barentswatch.backend.token.TokenFacade;
import pl.devcezz.barentswatch.backend.token.UserTokens;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/barentswatch/authentication")
public class AuthenticationResource {

    @Inject
    UserService userService;

    @Inject
    TokenFacade tokenFacade;

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response registerUser(@Valid RegistrationRequest request) {
        userService.registerUser(new UserCredentials(request.email(), request.password()));
        return Response.ok().build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/login")
    @VerifyClient
    public UserTokens generateAccessToken(UserCredentials request) {
        return tokenFacade.generateTokenFor(request);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/refreshtoken")
    public UserTokens refreshToken(String refreshToken) {
        return tokenFacade.generateApiTokenForRefreshToken(refreshToken);
    }

    record RegistrationRequest(@Email(regexp = ".+@.+\\..+") String email, @NotEmpty @Size(min = 4) String password) {
    }
}
