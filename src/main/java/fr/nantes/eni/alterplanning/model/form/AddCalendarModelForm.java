package fr.nantes.eni.alterplanning.model.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/*
 * Created by ughostephan on 23/06/2017.
 */
public class AddCalendarModelForm implements Serializable {

    @NotNull
    private Integer idCalendar;

    @NotNull
    @NotBlank
    @Size(min = 5, max = 50)
    private String name;

    public Integer getIdCalendar() {
        return idCalendar;
    }

    public void setIdCalendar(Integer idCalendar) {
        this.idCalendar = idCalendar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
