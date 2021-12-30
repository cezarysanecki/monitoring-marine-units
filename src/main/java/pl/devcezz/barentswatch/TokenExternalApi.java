package pl.devcezz.barentswatch;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;

@RegisterRestClient(configKey = "token-api")
public interface TokenExternalApi {

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    String fetchToken(Form form);
}