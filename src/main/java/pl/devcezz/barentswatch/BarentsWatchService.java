package pl.devcezz.barentswatch;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
class BarentsWatchService {

    String getAllVessels() {
        return "vessels";
    }
}
