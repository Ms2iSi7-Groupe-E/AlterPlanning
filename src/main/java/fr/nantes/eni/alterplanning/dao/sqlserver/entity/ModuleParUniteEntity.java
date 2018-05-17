package fr.nantes.eni.alterplanning.dao.sqlserver.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ModuleParUnite")
public class ModuleParUniteEntity {

    @Id
    @Column(name = "Id")
    private Integer id;

    @Column(name = "Position")
    private Integer position;

    @Column(name = "IdUnite")
    private Integer idUnite;

    @Column(name = "IdModule")
    private Integer idModule;

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

    public Integer getIdUnite() {
        return idUnite;
    }

    public void setIdUnite(Integer idUnite) {
        this.idUnite = idUnite;
    }

    public Integer getIdModule() {
        return idModule;
    }

    public void setIdModule(Integer idModule) {
        this.idModule = idModule;
    }
}
