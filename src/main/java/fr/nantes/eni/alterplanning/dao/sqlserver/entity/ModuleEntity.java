package fr.nantes.eni.alterplanning.dao.sqlserver.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Module")
public class ModuleEntity {

    @Id
    @Column(name = "IdModule")
    private Integer idModule;

    @Column(name = "Libelle")
    private String libelle;

    @Column(name = "LibelleCourt")
    private String libelleCourt;

    @Column(name = "DureeEnHeures")
    private Integer dureeEnHeures;

    @Column(name = "DureeEnSemaines")
    private Integer dureeEnSemaines;

    public Integer getIdModule() {
        return idModule;
    }

    public void setIdModule(Integer idModule) {
        this.idModule = idModule;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
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

    public Integer getDureeEnSemaines() {
        return dureeEnSemaines;
    }

    public void setDureeEnSemaines(Integer dureeEnSemaines) {
        this.dureeEnSemaines = dureeEnSemaines;
    }
}
