package fr.nantes.eni.alterplanning.dao.sqlserver.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "Formation")
public class FormationEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "CodeFormation")
    private String codeFormation;

    @Column(name = "LibelleLong")
    private String libelleLong;

    @Column(name = "LibelleCourt")
    private String libelleCourt;

    @Column(name = "DureeEnHeures")
    private Integer dureeEnHeures;

    @Column(name = "TauxHoraire")
    private Float tauxHoraire;

    @Column(name = "CodeTitre")
    private String codeTitre;

    @Column(name = "PrixPublicEnCours")
    private Float prixPublicEnCours;

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

    @Column(name = "Archiver")
    private Boolean archiver;

    @Column(name = "ECFaPasser")
    private Boolean eCFaPasser;

    @Column(name = "TypeFormation")
    private Integer typeFormation;

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

    public Float getTauxHoraire() {
        return tauxHoraire;
    }

    public void setTauxHoraire(Float tauxHoraire) {
        this.tauxHoraire = tauxHoraire;
    }

    public String getCodeTitre() {
        return codeTitre;
    }

    public void setCodeTitre(String codeTitre) {
        this.codeTitre = codeTitre;
    }

    public Float getPrixPublicEnCours() {
        return prixPublicEnCours;
    }

    public void setPrixPublicEnCours(Float prixPublicEnCours) {
        this.prixPublicEnCours = prixPublicEnCours;
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

    public Boolean getArchiver() {
        return archiver;
    }

    public void setArchiver(Boolean archiver) {
        this.archiver = archiver;
    }

    public Boolean geteCFaPasser() {
        return eCFaPasser;
    }

    public void seteCFaPasser(Boolean eCFaPasser) {
        this.eCFaPasser = eCFaPasser;
    }

    public Integer getTypeFormation() {
        return typeFormation;
    }

    public void setTypeFormation(Integer typeFormation) {
        this.typeFormation = typeFormation;
    }

    public Integer getCodeLieu() {
        return codeLieu;
    }

    public void setCodeLieu(Integer codeLieu) {
        this.codeLieu = codeLieu;
    }
}
