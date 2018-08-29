package fr.nantes.eni.alterplanning.model.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/*
 * Created by ughostephan on 23/06/2017.
 */
public class DuplicateCalendarModelForm implements Serializable {

    @NotNull
    @NotBlank
    @Size(min = 5, max = 50)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
