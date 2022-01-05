package pl.devcezz.barentswatch.backend.user;

import pl.devcezz.barentswatch.backend.user.entity.UserVessel;
import io.quarkus.mongodb.panache.PanacheMongoRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserVesselRepository implements PanacheMongoRepository<UserVessel> {}