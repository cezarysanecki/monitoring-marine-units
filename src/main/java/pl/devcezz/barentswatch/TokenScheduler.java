package pl.devcezz.barentswatch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.scheduler.Scheduled;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Form;

@ApplicationScoped
public class TokenScheduler {

    @Inject
    @RestClient
    TokenExternalApi tokenExternalApi;

    @Inject
    ObjectMapper objectMapper;

    @Scheduled(every = "30m")
    void fetchAccessToken() throws JsonProcessingException {
        Form form = new Form()
                .param("client_id", "xxx")
                .param("scope", "api")
                .param("client_secret", "xxx")
                .param("grant_type", "client_credentials");

        AccessToken accessToken = objectMapper.readValue(tokenExternalApi.fetchToken(form), AccessToken.class);

        Registry.accessToken.set(accessToken.accessToken());
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
record AccessToken(@JsonProperty(value = "access_token") String accessToken) {}
