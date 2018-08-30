package fr.nantes.eni.alterplanning.model.form;

import fr.nantes.eni.alterplanning.dao.mysql.entity.enums.CalendarState;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class StateForm implements Serializable {

    @NotNull
    private CalendarState state;

    public CalendarState getState() {
        return state;
    }

    public void setState(CalendarState state) {
        this.state = state;
    }
}
