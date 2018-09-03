package fr.nantes.eni.alterplanning.model.response;

import fr.nantes.eni.alterplanning.dao.mysql.entity.IndependantModuleEntity;
import fr.nantes.eni.alterplanning.model.simplebean.CoursComplet;

import java.util.ArrayList;
import java.util.List;

public class CoursGenerationResponse {

    private List<CoursComplet> cours = new ArrayList<>();

    private List<IndependantModuleEntity> independantModules = new ArrayList<>();

    public List<CoursComplet> getCours() {
        return cours;
    }

    public void setCours(List<CoursComplet> cours) {
        this.cours = cours;
    }

    public List<IndependantModuleEntity> getIndependantModules() {
        return independantModules;
    }

    public void setIndependantModules(List<IndependantModuleEntity> independantModules) {
        this.independantModules = independantModules;
    }

    public void addCours(final CoursComplet coursComplet) {
        cours.add(coursComplet);
    }

    public void addIndependantModules(final IndependantModuleEntity independantModuleEntity) {
        independantModules.add(independantModuleEntity);
    }
}
