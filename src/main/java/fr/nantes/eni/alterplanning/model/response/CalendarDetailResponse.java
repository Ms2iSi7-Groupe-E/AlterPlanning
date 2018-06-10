package fr.nantes.eni.alterplanning.model.response;

import fr.nantes.eni.alterplanning.dao.sqlserver.entity.CoursEntity;

import java.util.ArrayList;
import java.util.List;

public class CalendarDetailResponse extends CalendarResponse {
    private List<CoursEntity> cours = new ArrayList<>();

    // TODO : ajouter liste de contraintes

    public List<CoursEntity> getCours() {
        return cours;
    }

    public void setCours(List<CoursEntity> cours) {
        this.cours = cours;
    }
}
