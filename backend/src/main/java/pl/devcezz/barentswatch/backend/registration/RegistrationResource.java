package pl.devcezz.barentswatch.backend.registration;

import pl.devcezz.barentswatch.backend.security.Client;
import pl.devcezz.barentswatch.backend.security.ClientRepository;
import pl.devcezz.barentswatch.backend.security.exception.ClientAlreadyRegisteredException;

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
import java.util.Optional;

@Path("/barentswatch/registration")
public class RegistrationResource {

    @Inject
    ClientRepository clientRepository;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response registerClient(@Valid RegistrationRequest request) {
        Optional<Client> foundedClient = clientRepository.find("email", request.email()).firstResultOptional();

        foundedClient.ifPresentOrElse(
                client ->  { throw new ClientAlreadyRegisteredException(request.email()); },
                () -> clientRepository.add(request.email(), request.password(), "user")
        );

        return Response.ok().build();
    }
}

record RegistrationRequest(@Email(regexp = ".+@.+\\..+") String email, @NotEmpty @Size(min = 4) String password) {}
