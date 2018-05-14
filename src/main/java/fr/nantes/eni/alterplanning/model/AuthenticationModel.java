package fr.nantes.eni.alterplanning.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * Created by ughostephan on 23/06/2017.
 */
public class AuthenticationModel implements Serializable {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    public AuthenticationModel() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
