package fr.nantes.eni.alterplanning.dao.sqlserver.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Lieu")
public class LieuEntity {

    @Id
    @Column(name = "CodeLieu")
    private Integer codeLieu;

    @Column(name = "Libelle")
    private String libelle;

    @Column(name = "DebutAM")
    private String debutAM;

    @Column(name = "FinAM")
    private String finAM;

    @Column(name = "DebutPM")
    private String debutPM;

    @Column(name = "FinPM")
    private String finPM;

    @Column(name = "archive")
    private Integer archive;

    @Column(name = "GestionEmargement")
    private Integer gestionEmargement;

    @Column(name = "Adresse")
    private String adresse;

    @Column(name = "CP")
    private String cp;

    @Column(name = "Ville")
    private String ville;

    public Integer getCodeLieu() {
        return codeLieu;
    }

    public void setCodeLieu(Integer codeLieu) {
        this.codeLieu = codeLieu;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDebutAM() {
        return debutAM;
    }

    public void setDebutAM(String debutAM) {
        this.debutAM = debutAM;
    }

    public String getFinAM() {
        return finAM;
    }

    public void setFinAM(String finAM) {
        this.finAM = finAM;
    }

    public String getDebutPM() {
        return debutPM;
    }

    public void setDebutPM(String debutPM) {
        this.debutPM = debutPM;
    }

    public String getFinPM() {
        return finPM;
    }

    public void setFinPM(String finPM) {
        this.finPM = finPM;
    }

    public Integer getArchive() {
        return archive;
    }

    public void setArchive(Integer archive) {
        this.archive = archive;
    }

    public Integer getGestionEmargement() {
        return gestionEmargement;
    }

    public void setGestionEmargement(Integer gestionEmargement) {
        this.gestionEmargement = gestionEmargement;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }
}
