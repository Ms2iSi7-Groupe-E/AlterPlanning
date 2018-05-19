package fr.nantes.eni.alterplanning.dao.sqlserver.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "StagiaireParEntreprise")
public class StagiaireParEntrepriseEntity {

    @Id
    @Column(name = "NumLien")
    private Integer numLien;

    @Column(name = "CodeStagiaire")
    private Integer codeStagiaire;

    @Column(name = "CodeEntreprise")
    private Integer codeEntreprise;

    public Integer getNumLien() {
        return numLien;
    }

    public void setNumLien(Integer numLien) {
        this.numLien = numLien;
    }

    public Integer getCodeStagiaire() {
        return codeStagiaire;
    }

    public void setCodeStagiaire(Integer codeStagiaire) {
        this.codeStagiaire = codeStagiaire;
    }

    public Integer getCodeEntreprise() {
        return codeEntreprise;
    }

    public void setCodeEntreprise(Integer codeEntreprise) {
        this.codeEntreprise = codeEntreprise;
    }
}
