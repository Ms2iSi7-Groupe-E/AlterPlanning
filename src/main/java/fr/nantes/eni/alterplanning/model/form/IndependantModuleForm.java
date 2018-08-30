package fr.nantes.eni.alterplanning.model.form;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

public class IndependantModuleForm implements Serializable {

    @NotNull
    @NotBlank
    @Size(min = 5, max = 20)
    private String shortName;

    @NotNull
    @NotBlank
    @Size(min = 5, max = 200)
    private String longName;

    @NotNull
    @NotEmpty
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date startDate;

    @NotNull
    @NotEmpty
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date endDate;

    @NotNull
    private Integer codeLieu;

    @NotNull
    private Integer volumeHoraire;

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
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

    public Integer getCodeLieu() {
        return codeLieu;
    }

    public void setCodeLieu(Integer codeLieu) {
        this.codeLieu = codeLieu;
    }

    public Integer getVolumeHoraire() {
        return volumeHoraire;
    }

    public void setVolumeHoraire(Integer volumeHoraire) {
        this.volumeHoraire = volumeHoraire;
    }
}
