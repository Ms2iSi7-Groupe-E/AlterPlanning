package fr.nantes.eni.alterplanning.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.nantes.eni.alterplanning.dao.mysql.entity.CalendarConstraintEntity;
import fr.nantes.eni.alterplanning.dao.mysql.entity.CalendarCoursEntity;
import fr.nantes.eni.alterplanning.dao.mysql.entity.IndependantModuleEntity;
import fr.nantes.eni.alterplanning.dao.mysql.entity.enums.CalendarState;
import fr.nantes.eni.alterplanning.dao.sqlserver.entity.CoursEntity;
import fr.nantes.eni.alterplanning.dao.sqlserver.entity.EntrepriseEntity;
import fr.nantes.eni.alterplanning.dao.sqlserver.entity.StagiaireEntity;
import fr.nantes.eni.alterplanning.util.AlterDateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CalendarResponse {

    private int id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AlterDateUtil.yyyyMMdd, timezone=AlterDateUtil.timezone)
    private Date startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AlterDateUtil.yyyyMMdd, timezone=AlterDateUtil.timezone)
    private Date endDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AlterDateUtil.ddMMyyyyHHmmss, timezone=AlterDateUtil.timezone)
    private Date createdAt;

    private CalendarState state;

    private Boolean isModel;

    private StagiaireEntity stagiaire;

    private EntrepriseEntity entreprise;

    private List<IndependantModuleEntity> independantModules = new ArrayList<>();

    private List<CalendarConstraintEntity> constraints = new ArrayList<>();

    private List<CoursEntity> cours = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public CalendarState getState() {
        return state;
    }

    public void setState(CalendarState state) {
        this.state = state;
    }

    @JsonProperty("isModel")
    public Boolean getModel() {
        return isModel;
    }

    public void setModel(Boolean model) {
        isModel = model;
    }

    public StagiaireEntity getStagiaire() {
        return stagiaire;
    }

    public void setStagiaire(StagiaireEntity stagiaire) {
        this.stagiaire = stagiaire;
    }

    public EntrepriseEntity getEntreprise() {
        return entreprise;
    }

    public void setEntreprise(EntrepriseEntity entreprise) {
        this.entreprise = entreprise;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public List<CalendarConstraintEntity> getConstraints() {
        return constraints;
    }

    public void setConstraints(List<CalendarConstraintEntity> constraints) {
        this.constraints = constraints;
    }

    public List<CoursEntity> getCours() {
        return cours;
    }

    public void setCours(List<CoursEntity> cours) {
        this.cours = cours;
    }

    public List<IndependantModuleEntity> getIndependantModules() {
        return independantModules;
    }

    public void setIndependantModules(List<IndependantModuleEntity> independantModules) {
        this.independantModules = independantModules;
    }
}
