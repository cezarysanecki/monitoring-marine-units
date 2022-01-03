package pl.devcezz.barentswatch.security;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "client")
public class Client {

    @Id
    @GeneratedValue
    public Long id;
    public String email;
    public String password;
    public String role;
}
