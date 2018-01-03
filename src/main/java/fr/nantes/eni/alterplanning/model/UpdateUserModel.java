package fr.nantes.eni.alterplanning.model;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Created by ughostephan on 23/06/2017.
 */
public class UpdateUserModel implements Serializable, IUserModel {

    @Size(max = 100)
    private String firstname;

    @Size(max = 100)
    private String lastname;

    @Email
    @Size(max = 100)
    private String email;

    private String birthday;

    @Size(max = 100)
    private String city;

    @Size(max = 100)
    private String country;

    public UpdateUserModel() {
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

}
