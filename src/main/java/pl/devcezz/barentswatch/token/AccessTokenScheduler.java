package pl.devcezz.barentswatch.token;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.scheduler.Scheduled;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import pl.devcezz.barentswatch.Registry;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Form;

@ApplicationScoped
public class AccessTokenScheduler {

    @Inject
    @RestClient
    AccessTokenExternalApi accessTokenExternalApi;

    @Inject
    AccessTokenProperties accessTokenProperties;

    @Inject
    ObjectMapper objectMapper;

    @Scheduled(every = "${barents-watch.token.scheduled.every:30m}")
    void fetchAccessToken() throws JsonProcessingException {
        Form form = new Form()
                .param("client_id", accessTokenProperties.clientId)
                .param("scope", accessTokenProperties.scope)
                .param("client_secret", accessTokenProperties.clientSecret)
                .param("grant_type", accessTokenProperties.grantType);

        Token token = objectMapper.readValue(accessTokenExternalApi.fetchToken(form), Token.class);

        Registry.accessToken.set("Bearer " + token.value());
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
record Token(@JsonProperty(value = "access_token") String value) {}
