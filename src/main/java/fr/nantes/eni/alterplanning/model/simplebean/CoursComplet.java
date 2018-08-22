package fr.nantes.eni.alterplanning.model.simplebean;

import java.util.Date;

public class CoursComplet {

    private String idCours;
    private Integer idModule;
    private String libelleCours;
    private String libelleModule;
    private String libelleFormation;
    private String libelleFormationLong;
    private Integer codeLieu;
    private Date debut;
    private Date fin;
    private Integer dureeReelleEnHeures;
    private String codeFormation;

    public String getIdCours() {
        return idCours;
    }

    public void setIdCours(String idCours) {
        this.idCours = idCours;
    }

    public Integer getIdModule() {
        return idModule;
    }

    public void setIdModule(Integer idModule) {
        this.idModule = idModule;
    }

    public String getLibelleCours() {
        return libelleCours;
    }

    public void setLibelleCours(String libelleCours) {
        this.libelleCours = libelleCours;
    }

    public String getLibelleModule() {
        return libelleModule;
    }

    public void setLibelleModule(String libelleModule) {
        this.libelleModule = libelleModule;
    }

    public String getLibelleFormation() {
        return libelleFormation;
    }

    public void setLibelleFormation(String libelleFormation) {
        this.libelleFormation = libelleFormation;
    }

    public String getLibelleFormationLong() {
        return libelleFormationLong;
    }

    public void setLibelleFormationLong(String libelleFormationLong) {
        this.libelleFormationLong = libelleFormationLong;
    }

    public Integer getCodeLieu() {
        return codeLieu;
    }

    public void setCodeLieu(Integer codeLieu) {
        this.codeLieu = codeLieu;
    }

    public Date getDebut() {
        return debut;
    }

    public void setDebut(Date debut) {
        this.debut = debut;
    }

    public Date getFin() {
        return fin;
    }

    public void setFin(Date fin) {
        this.fin = fin;
    }

    public Integer getDureeReelleEnHeures() {
        return dureeReelleEnHeures;
    }

    public void setDureeReelleEnHeures(Integer dureeReelleEnHeures) {
        this.dureeReelleEnHeures = dureeReelleEnHeures;
    }

    public String getCodeFormation() {
        return codeFormation;
    }

    public void setCodeFormation(String codeFormation) {
        this.codeFormation = codeFormation;
    }
}
