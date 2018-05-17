package fr.nantes.eni.alterplanning.dao.sqlserver.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Cours")
public class CoursEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "IdCours")
    private String idCours;

    @Column(name = "LibelleCours")
    private String libelleCours;

    @Column(name = "IdModule")
    private Integer idModule;

    @Column(name = "CodeLieu")
    private Integer codeLieu;

    @Column(name = "CodeFormateur")
    private String codeFormateur;

    @Column(name = "CodeSalle")
    private String codeSalle;

    @Column(name = "DateAdefinir")
    private Boolean dateAdefinir;

    @Column(name = "DureePrevueEnHeures")
    private Integer dureePrevueEnHeures;

    @Column(name = "DureeReelleEnHeures")
    private Integer dureeReelleEnHeures;

    @Column(name = "CodePromotion")
    private String codePromotion;

    @Column(name = "PrixPublicAffecte")
    private String prixPublicAffecte;

    @Column(name = "Debut")
    @Temporal(TemporalType.DATE)
    private Date debut;

    @Column(name = "Fin")
    @Temporal(TemporalType.DATE)
    private Date fin;

    public String getIdCours() {
        return idCours;
    }

    public void setIdCours(String idCours) {
        this.idCours = idCours;
    }

    public String getLibelleCours() {
        return libelleCours;
    }

    public void setLibelleCours(String libelleCours) {
        this.libelleCours = libelleCours;
    }

    public Integer getIdModule() {
        return idModule;
    }

    public void setIdModule(Integer idModule) {
        this.idModule = idModule;
    }

    public Integer getCodeLieu() {
        return codeLieu;
    }

    public void setCodeLieu(Integer codeLieu) {
        this.codeLieu = codeLieu;
    }

    public String getCodeFormateur() {
        return codeFormateur;
    }

    public void setCodeFormateur(String codeFormateur) {
        this.codeFormateur = codeFormateur;
    }

    public String getCodeSalle() {
        return codeSalle;
    }

    public void setCodeSalle(String codeSalle) {
        this.codeSalle = codeSalle;
    }

    public Boolean getDateAdefinir() {
        return dateAdefinir;
    }

    public void setDateAdefinir(Boolean dateAdefinir) {
        this.dateAdefinir = dateAdefinir;
    }

    public Integer getDureePrevueEnHeures() {
        return dureePrevueEnHeures;
    }

    public void setDureePrevueEnHeures(Integer dureePrevueEnHeures) {
        this.dureePrevueEnHeures = dureePrevueEnHeures;
    }

    public Integer getDureeReelleEnHeures() {
        return dureeReelleEnHeures;
    }

    public void setDureeReelleEnHeures(Integer dureeReelleEnHeures) {
        this.dureeReelleEnHeures = dureeReelleEnHeures;
    }

    public String getCodePromotion() {
        return codePromotion;
    }

    public void setCodePromotion(String codePromotion) {
        this.codePromotion = codePromotion;
    }

    public String getPrixPublicAffecte() {
        return prixPublicAffecte;
    }

    public void setPrixPublicAffecte(String prixPublicAffecte) {
        this.prixPublicAffecte = prixPublicAffecte;
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
}
