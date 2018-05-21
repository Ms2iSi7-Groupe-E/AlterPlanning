package fr.nantes.eni.alterplanning.dao.mysql.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;

/**
 * Created by ughostephan on 23/06/2017.
 */
@Entity
@Table(name = "users", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"email"})
})
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "email")
    @Size(max = 100)
    @NotNull
    private String email;

    @Column(name = "name")
    @Size(max = 100)
    @NotNull
    private String name;

    @Column(name = "password")
    @Size(max = 200)
    @NotNull
    @JsonIgnore
    private String password;

    @Column(name = "is_admin")
    @NotNull
    private boolean admin = false;

    @Column(name = "is_active")
    @NotNull
    @JsonIgnore
    private boolean active = true;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return email;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return active;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return active;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return active;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return active;
    }
}
