package fr.nantes.eni.alterplanning.dao.mysql.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.nantes.eni.alterplanning.dao.mysql.entity.enums.CalendarState;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by ughostephan on 23/06/2017.
 */
@Entity
@Table(name = "calendars")
public class CalendarEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "stagiaireId")
    private Integer stagiaireId;

    @Column(name = "entrepriseId")
    private Integer entrepriseId;

    @Column(name = "startDate")
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Column(name = "endDate")
    @Temporal(TemporalType.DATE)
    private Date endDate;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private CalendarState state;

    @Column(name = "isModel")
    private Boolean isModel = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getStagiaireId() {
        return stagiaireId;
    }

    public void setStagiaireId(Integer stagiaireId) {
        this.stagiaireId = stagiaireId;
    }

    public Integer getEntrepriseId() {
        return entrepriseId;
    }

    public void setEntrepriseId(Integer entrepriseId) {
        this.entrepriseId = entrepriseId;
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
}
