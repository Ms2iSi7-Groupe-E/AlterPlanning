package fr.nantes.eni.alterplanning.dao.sqlserver.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Stagiaire")
public class StagiaireEntity {

    @Id
    @Column(name = "CodeStagiaire")
    private Integer codeStagiaire;

    @Column(name = "Nom")
    private String nom;

    @Column(name = "Prenom")
    private String prenom;

    @Column(name = "Email")
    private String email;

    public Integer getCodeStagiaire() {
        return codeStagiaire;
    }

    public void setCodeStagiaire(Integer codeStagiaire) {
        this.codeStagiaire = codeStagiaire;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
