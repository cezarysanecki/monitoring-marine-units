package pl.devcezz.barentswatch.user.entity;

import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

@MongoEntity(collection="userVessel")
public class UserVessel {

    public ObjectId id;
    public String email;
    public List<Vessel> vessels = new ArrayList<>();

    public boolean containsVessel(Integer mmsi) {
        return vessels.stream()
                .anyMatch(vessel -> vessel.mmsi.equals(mmsi));
    }
}

