package fr.nantes.eni.alterplanning.dao.sqlserver.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Titre")
public class TitreEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "CodeTitre")
    private String codeTitre;

    @Column(name = "LibelleCourt")
    private String libelleCourt;

    @Column(name = "LibelleLong")
    private String libelleLong;

    @Column(name = "TitreENI")
    private Boolean titreENI;

    @Column(name = "Archiver")
    private Boolean archiver;

    @Column(name = "Niveau")
    private String niveau;

    @Column(name = "DateArrete")
    @Temporal(TemporalType.DATE)
    private Date dateArrete;

    @Column(name = "DateJO")
    @Temporal(TemporalType.DATE)
    private Date dateJO;

    @Column(name = "NumeroJO")
    private Integer numeroJO;

    @Column(name = "CodeAFPA")
    private String codeAFPA;

    @Column(name = "Millesime")
    private String millesime;

    public String getCodeTitre() {
        return codeTitre;
    }

    public void setCodeTitre(String codeTitre) {
        this.codeTitre = codeTitre;
    }

    public String getLibelleCourt() {
        return libelleCourt;
    }

    public void setLibelleCourt(String libelleCourt) {
        this.libelleCourt = libelleCourt;
    }

    public String getLibelleLong() {
        return libelleLong;
    }

    public void setLibelleLong(String libelleLong) {
        this.libelleLong = libelleLong;
    }

    public Boolean getTitreENI() {
        return titreENI;
    }

    public void setTitreENI(Boolean titreENI) {
        this.titreENI = titreENI;
    }

    public Boolean getArchiver() {
        return archiver;
    }

    public void setArchiver(Boolean archiver) {
        this.archiver = archiver;
    }

    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    public Date getDateArrete() {
        return dateArrete;
    }

    public void setDateArrete(Date dateArrete) {
        this.dateArrete = dateArrete;
    }

    public Date getDateJO() {
        return dateJO;
    }

    public void setDateJO(Date dateJO) {
        this.dateJO = dateJO;
    }

    public Integer getNumeroJO() {
        return numeroJO;
    }

    public void setNumeroJO(Integer numeroJO) {
        this.numeroJO = numeroJO;
    }

    public String getCodeAFPA() {
        return codeAFPA;
    }

    public void setCodeAFPA(String codeAFPA) {
        this.codeAFPA = codeAFPA;
    }

    public String getMillesime() {
        return millesime;
    }

    public void setMillesime(String millesime) {
        this.millesime = millesime;
    }
}
