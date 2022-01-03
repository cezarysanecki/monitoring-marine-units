package pl.devcezz.barentswatch.security;

import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "client")
@UserDefinition
public class Client {

    @Id
    @GeneratedValue
    public Long id;
    @Username
    public String username;
    @Password
    public String password;
    @Roles
    public String role;
}
