package pl.devcezz.barentswatch.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Produces;

@ApplicationScoped
public final class AppConfig {

    @Produces
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}