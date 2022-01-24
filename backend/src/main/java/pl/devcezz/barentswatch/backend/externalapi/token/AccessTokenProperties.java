package pl.devcezz.barentswatch.backend.externalapi.token;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Singleton;

@Singleton
class AccessTokenProperties {

    @ConfigProperty(name = "barents-watch.access.client-id")
    public String clientId;

    @ConfigProperty(name = "barents-watch.access.scope")
    public String scope;

    @ConfigProperty(name = "barents-watch.access.client-secret")
    public String clientSecret;

    @ConfigProperty(name = "barents-watch.access.grant-type")
    public String grantType;
}
