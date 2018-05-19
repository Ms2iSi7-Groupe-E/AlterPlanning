package fr.nantes.eni.alterplanning.dao.sqlserver.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Entreprise")
public class EntrepriseEntity {

    @Id
    @Column(name = "CodeEntreprise")
    private Integer codeEntreprise;

    @Column(name = "RaisonSociale")
    private String raisonSociale;

    @Column(name = "Email")
    private String email;

    public Integer getCodeEntreprise() {
        return codeEntreprise;
    }

    public void setCodeEntreprise(Integer codeEntreprise) {
        this.codeEntreprise = codeEntreprise;
    }

    public String getRaisonSociale() {
        return raisonSociale;
    }

    public void setRaisonSociale(String raisonSociale) {
        this.raisonSociale = raisonSociale;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
