package fr.nantes.eni.alterplanning.model.form;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by ughostephan on 23/06/2017.
 */
public class AddCalendarForm implements Serializable {

    private Integer stagiaireId;

    private Integer entrepriseId;

    @DateTimeFormat(pattern="dd-MMM-yyyy")
    private Date startDate;

    @DateTimeFormat(pattern="dd-MMM-yyyy")
    private Date endDate;

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
}
