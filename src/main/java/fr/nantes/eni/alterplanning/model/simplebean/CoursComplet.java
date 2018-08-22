package fr.nantes.eni.alterplanning.model.simplebean;

import java.util.Date;

public class CoursComplet {

    private String IdCours;
    private Integer IdModule;
    private String LibelleCours;
    private String LibelleModule;
    private String LibelleFormation;
    private String LibelleFormationLong;
    private Integer CodeLieu;
    private Date Debut;
    private Date Fin;

    public String getIdCours() {
        return IdCours;
    }

    public void setIdCours(String idCours) {
        IdCours = idCours;
    }

    public Integer getIdModule() {
        return IdModule;
    }

    public void setIdModule(Integer idModule) {
        IdModule = idModule;
    }

    public String getLibelleCours() {
        return LibelleCours;
    }

    public void setLibelleCours(String libelleCours) {
        LibelleCours = libelleCours;
    }

    public String getLibelleModule() {
        return LibelleModule;
    }

    public void setLibelleModule(String libelleModule) {
        LibelleModule = libelleModule;
    }

    public String getLibelleFormation() {
        return LibelleFormation;
    }

    public void setLibelleFormation(String libelleFormation) {
        LibelleFormation = libelleFormation;
    }

    public String getLibelleFormationLong() {
        return LibelleFormationLong;
    }

    public void setLibelleFormationLong(String libelleFormationLong) {
        LibelleFormationLong = libelleFormationLong;
    }

    public Integer getCodeLieu() {
        return CodeLieu;
    }

    public void setCodeLieu(Integer codeLieu) {
        CodeLieu = codeLieu;
    }

    public Date getDebut() {
        return Debut;
    }

    public void setDebut(Date debut) {
        Debut = debut;
    }

    public Date getFin() {
        return Fin;
    }

    public void setFin(Date fin) {
        Fin = fin;
    }
}
