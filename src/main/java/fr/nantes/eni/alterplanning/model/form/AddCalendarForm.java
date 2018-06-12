package fr.nantes.eni.alterplanning.model.form;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ughostephan on 23/06/2017.
 */
public class AddCalendarForm implements Serializable {

    private Integer stagiaireId;

    private Integer entrepriseId;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date startDate;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date endDate;

    @NotNull
    @NotEmpty
    private List<ConstraintForm> constraints = new ArrayList<>();

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

    public List<ConstraintForm> getConstraints() {
        return constraints;
    }

    public void setConstraints(List<ConstraintForm> constraints) {
        this.constraints = constraints;
    }
}
