package fr.nantes.eni.alterplanning.model.response;

import java.util.ArrayList;
import java.util.List;

public class ModuleRequirementResponse {

    private int moduleId;

    private List<RequirementResponse> requirements = new ArrayList<>();

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public List<RequirementResponse> getRequirements() {
        return requirements;
    }

    public void setRequirements(List<RequirementResponse> requirements) {
        this.requirements = requirements;
    }

    public void addRequirement(final RequirementResponse requirement) {
        this.requirements.add(requirement);
    }
}
