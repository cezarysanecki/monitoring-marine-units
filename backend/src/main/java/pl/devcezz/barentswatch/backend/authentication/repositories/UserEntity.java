package pl.devcezz.barentswatch.backend.authentication.repositories;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "app_user")
public class UserEntity {

    @Id
    @GeneratedValue
    public Long id;
    public String email;
    public String password;
    public String role;
}
