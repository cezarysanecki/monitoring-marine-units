package pl.devcezz.barentswatch.backend;

import java.util.concurrent.atomic.AtomicReference;

public class Registry {

    public static AtomicReference<String> accessToken = new AtomicReference<>();
}
