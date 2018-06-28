package fr.nantes.eni.alterplanning.model.response;

public class RequirementResponse {

    private int idModuleRequirement;

    private int moduleId;

    private boolean or;

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public boolean isOr() {
        return or;
    }

    public void setOr(boolean or) {
        this.or = or;
    }

    public int getIdModuleRequirement() {
        return idModuleRequirement;
    }

    public void setIdModuleRequirement(int idModuleRequirement) {
        this.idModuleRequirement = idModuleRequirement;
    }
}
