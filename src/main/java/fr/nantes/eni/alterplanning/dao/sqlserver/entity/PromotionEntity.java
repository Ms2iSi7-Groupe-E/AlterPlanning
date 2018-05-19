package fr.nantes.eni.alterplanning.dao.sqlserver.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Promotion")
public class PromotionEntity {

    @Id
    @Column(name = "CodePromotion")
    private String codePromotion;

    @Column(name = "Libelle")
    private String libelle;

    @Column(name = "CodeFormation")
    private String codeFormation;

    @Column(name = "CodeLieu")
    private Integer codeLieu;

    @Column(name = "Debut")
    @Temporal(TemporalType.DATE)
    private Date debut;

    @Column(name = "Fin")
    @Temporal(TemporalType.DATE)
    private Date fin;

    public String getCodePromotion() {
        return codePromotion;
    }

    public void setCodePromotion(String codePromotion) {
        this.codePromotion = codePromotion;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getCodeFormation() {
        return codeFormation;
    }

    public void setCodeFormation(String codeFormation) {
        this.codeFormation = codeFormation;
    }

    public Integer getCodeLieu() {
        return codeLieu;
    }

    public void setCodeLieu(Integer codeLieu) {
        this.codeLieu = codeLieu;
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
