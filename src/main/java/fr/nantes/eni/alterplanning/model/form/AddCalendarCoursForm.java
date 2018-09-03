package fr.nantes.eni.alterplanning.model.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
 * Created by ughostephan on 23/06/2017.
 */
public class AddCalendarCoursForm implements Serializable {

    @NotNull
    @NotEmpty
    private List<String> coursIds = new ArrayList<>();

    @NotNull
    private List<Integer> coursIndependantIds = new ArrayList<>();

    public List<String> getCoursIds() {
        return coursIds;
    }

    public void setCoursIds(List<String> coursIds) {
        this.coursIds = coursIds;
    }

    public List<Integer> getCoursIndependantIds() {
        return coursIndependantIds;
    }

    public void setCoursIndependantIds(List<Integer> coursIndependantIds) {
        this.coursIndependantIds = coursIndependantIds;
    }
}
