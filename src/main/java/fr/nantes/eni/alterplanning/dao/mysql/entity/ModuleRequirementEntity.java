package fr.nantes.eni.alterplanning.dao.mysql.entity;

import javax.persistence.*;

/**
 * Created by ughostephan on 23/06/2017.
 */
@Entity
@Table(name = "module_requirements")
public class ModuleRequirementEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "moduleId")
    private int moduleId;

    @Column(name = "requiredModuleId")
    private int requiredModuleId;

    @Column(name = "isOr")
    private boolean or = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public int getRequiredModuleId() {
        return requiredModuleId;
    }

    public void setRequiredModuleId(int requiredModuleId) {
        this.requiredModuleId = requiredModuleId;
    }

    public boolean isOr() {
        return or;
    }

    public void setOr(boolean or) {
        this.or = or;
    }
}
