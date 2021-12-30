package pl.devcezz.barentswatch;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Singleton;

@Singleton
public class Access {

    @ConfigProperty(name = "barents-watch.access.client-id")
    String clientId;

    @ConfigProperty(name = "barents-watch.access.scope")
    String scope;

    @ConfigProperty(name = "barents-watch.access.client-secret")
    String clientSecret;

    @ConfigProperty(name = "barents-watch.access.grant-type")
    String grantType;
}
