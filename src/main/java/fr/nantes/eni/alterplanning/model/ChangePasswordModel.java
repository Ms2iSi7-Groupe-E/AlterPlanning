package fr.nantes.eni.alterplanning.model;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Created by ughostephan on 23/06/2017.
 */
public class ChangePasswordModel implements Serializable {

    @NotBlank
    private String old_password;

    @NotBlank
    @Size(min = 8)
    private String new_password;

    public ChangePasswordModel() {
    }

    public String getOld_password() {
        return old_password;
    }

    public void setOld_password(String old_password) {
        this.old_password = old_password;
    }

    public String getNew_password() {
        return new_password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }
}
