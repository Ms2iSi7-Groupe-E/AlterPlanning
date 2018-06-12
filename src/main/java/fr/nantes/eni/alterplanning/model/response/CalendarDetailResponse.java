package fr.nantes.eni.alterplanning.model.response;

import fr.nantes.eni.alterplanning.dao.mysql.entity.CalendarConstraintEntity;
import fr.nantes.eni.alterplanning.dao.sqlserver.entity.CoursEntity;

import java.util.ArrayList;
import java.util.List;

public class CalendarDetailResponse extends CalendarResponse {
    private List<CoursEntity> cours = new ArrayList<>();

    private List<CalendarConstraintEntity> constraints = new ArrayList<>();

    public List<CoursEntity> getCours() {
        return cours;
    }

    public void setCours(List<CoursEntity> cours) {
        this.cours = cours;
    }

    public List<CalendarConstraintEntity> getConstraints() {
        return constraints;
    }

    public void setConstraints(List<CalendarConstraintEntity> constraints) {
        this.constraints = constraints;
    }
}
