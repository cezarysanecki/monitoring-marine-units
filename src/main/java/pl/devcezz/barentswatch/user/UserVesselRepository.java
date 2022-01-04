package pl.devcezz.barentswatch.user;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import pl.devcezz.barentswatch.user.entity.UserVessel;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserVesselRepository implements PanacheMongoRepository<UserVessel> {}