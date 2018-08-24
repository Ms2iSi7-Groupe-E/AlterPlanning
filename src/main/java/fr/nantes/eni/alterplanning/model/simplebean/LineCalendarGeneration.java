package fr.nantes.eni.alterplanning.model.simplebean;

import fr.nantes.eni.alterplanning.dao.sqlserver.entity.LieuEntity;

public class LineCalendarGeneration {

    private LieuEntity lieu;
    private String libelle = "";
    private String debut = "../../..";
    private String fin = "../../..";
    private Integer dureeReelleEnHeures;
    private boolean entreprisePeriode = false;

    public LieuEntity getLieu() {
        return lieu;
    }

    public void setLieu(LieuEntity lieu) {
        this.lieu = lieu;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDebut() {
        return debut;
    }

    public void setDebut(String debut) {
        this.debut = debut;
    }

    public String getFin() {
        return fin;
    }

    public void setFin(String fin) {
        this.fin = fin;
    }

    public Integer getDureeReelleEnHeures() {
        return dureeReelleEnHeures;
    }

    public void setDureeReelleEnHeures(Integer dureeReelleEnHeures) {
        this.dureeReelleEnHeures = dureeReelleEnHeures;
    }

    public boolean isEntreprisePeriode() {
        return entreprisePeriode;
    }

    public void setEntreprisePeriode(boolean entreprisePeriode) {
        this.entreprisePeriode = entreprisePeriode;
    }
}
