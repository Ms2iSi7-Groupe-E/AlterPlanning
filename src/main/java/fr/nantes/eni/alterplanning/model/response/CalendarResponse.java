package fr.nantes.eni.alterplanning.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.nantes.eni.alterplanning.dao.mysql.entity.enums.CalendarState;
import fr.nantes.eni.alterplanning.dao.sqlserver.entity.EntrepriseEntity;
import fr.nantes.eni.alterplanning.dao.sqlserver.entity.StagiaireEntity;

import java.util.Date;

public class CalendarResponse {

    private int id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone="Europe/Paris")
    private Date startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone="Europe/Paris")
    private Date endDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone="Europe/Paris")
    private Date createdAt;

    private CalendarState state;

    private Boolean isModel;

    private StagiaireEntity stagiaire;

    private EntrepriseEntity entreprise;

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
}
