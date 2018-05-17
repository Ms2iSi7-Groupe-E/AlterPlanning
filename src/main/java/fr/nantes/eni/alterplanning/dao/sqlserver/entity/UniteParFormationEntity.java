package fr.nantes.eni.alterplanning.dao.sqlserver.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "UniteParFormation")
public class UniteParFormationEntity {

    @Id
    @Column(name = "Id")
    private Integer id;

    @Column(name = "Position")
    private Integer position;

    @Column(name = "IdUniteFormation")
    private Integer idUniteFormation;

    @Column(name = "CodeFormation")
    private String codeFormation;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getIdUniteFormation() {
        return idUniteFormation;
    }

    public void setIdUniteFormation(Integer idUniteFormation) {
        this.idUniteFormation = idUniteFormation;
    }

    public String getCodeFormation() {
        return codeFormation;
    }

    public void setCodeFormation(String codeFormation) {
        this.codeFormation = codeFormation;
    }
}
