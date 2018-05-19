package fr.nantes.eni.alterplanning.dao.sqlserver.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Formation")
public class FormationEntity {

    @Id
    @Column(name = "CodeFormation")
    private String codeFormation;

    @Column(name = "LibelleLong")
    private String libelleLong;

    @Column(name = "LibelleCourt")
    private String libelleCourt;

    @Column(name = "DureeEnHeures")
    private Integer dureeEnHeures;

    @Column(name = "CodeTitre")
    private String codeTitre;

    @Column(name = "HeuresCentre")
    private Integer heuresCentre;

    @Column(name = "HeuresStage")
    private Integer heuresStage;

    @Column(name = "SemainesCentre")
    private Integer semainesCentre;

    @Column(name = "SemainesStage")
    private Integer semainesStage;

    @Column(name = "DureeEnSemaines")
    private Integer dureeEnSemaines;

    @Column(name = "CodeLieu")
    private Integer codeLieu;

    public String getCodeFormation() {
        return codeFormation;
    }

    public void setCodeFormation(String codeFormation) {
        this.codeFormation = codeFormation;
    }

    public String getLibelleLong() {
        return libelleLong;
    }

    public void setLibelleLong(String libelleLong) {
        this.libelleLong = libelleLong;
    }

    public String getLibelleCourt() {
        return libelleCourt;
    }

    public void setLibelleCourt(String libelleCourt) {
        this.libelleCourt = libelleCourt;
    }

    public Integer getDureeEnHeures() {
        return dureeEnHeures;
    }

    public void setDureeEnHeures(Integer dureeEnHeures) {
        this.dureeEnHeures = dureeEnHeures;
    }

    public String getCodeTitre() {
        return codeTitre;
    }

    public void setCodeTitre(String codeTitre) {
        this.codeTitre = codeTitre;
    }

    public Integer getHeuresCentre() {
        return heuresCentre;
    }

    public void setHeuresCentre(Integer heuresCentre) {
        this.heuresCentre = heuresCentre;
    }

    public Integer getHeuresStage() {
        return heuresStage;
    }

    public void setHeuresStage(Integer heuresStage) {
        this.heuresStage = heuresStage;
    }

    public Integer getSemainesCentre() {
        return semainesCentre;
    }

    public void setSemainesCentre(Integer semainesCentre) {
        this.semainesCentre = semainesCentre;
    }

    public Integer getSemainesStage() {
        return semainesStage;
    }

    public void setSemainesStage(Integer semainesStage) {
        this.semainesStage = semainesStage;
    }

    public Integer getDureeEnSemaines() {
        return dureeEnSemaines;
    }

    public void setDureeEnSemaines(Integer dureeEnSemaines) {
        this.dureeEnSemaines = dureeEnSemaines;
    }

    public Integer getCodeLieu() {
        return codeLieu;
    }

    public void setCodeLieu(Integer codeLieu) {
        this.codeLieu = codeLieu;
    }
}
