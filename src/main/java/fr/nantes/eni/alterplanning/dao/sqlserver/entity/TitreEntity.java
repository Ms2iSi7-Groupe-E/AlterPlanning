package fr.nantes.eni.alterplanning.dao.sqlserver.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Titre")
public class TitreEntity {

    @Id
    @Column(name = "CodeTitre")
    private String codeTitre;

    @Column(name = "LibelleCourt")
    private String libelleCourt;

    @Column(name = "LibelleLong")
    private String libelleLong;

    @Column(name = "Niveau")
    private String niveau;

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

    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }
}
