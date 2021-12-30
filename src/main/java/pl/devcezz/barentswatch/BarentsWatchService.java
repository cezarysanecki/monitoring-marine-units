package pl.devcezz.barentswatch;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
class BarentsWatchService {

    String getVesselsPositionFor(BoundingBox boundingBox) {
        return "vessels";
    }
}
