package fr.nantes.eni.alterplanning.model.response;

public class RequirementResponse {

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
}
