package fr.nantes.eni.alterplanning.model.form;


import fr.nantes.eni.alterplanning.model.form.validator.IUserMailValidator;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Created by ughostephan on 23/06/2017.
 */
public class UpdateUserForm implements Serializable, IUserMailValidator {

    @Size(max = 100)
    private String name;

    @Email
    @Size(max = 100)
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
