package fr.nantes.eni.alterplanning.model.form;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by ughostephan on 23/06/2017.
 */
public class AddModuleRequirementForm implements Serializable {

    @NotNull
    private Integer requiredModuleId;

    @NotNull
    private Boolean isOr = false;

    public Integer getRequiredModuleId() {
        return requiredModuleId;
    }

    public void setRequiredModuleId(Integer requiredModuleId) {
        this.requiredModuleId = requiredModuleId;
    }

    public Boolean getOr() {
        return isOr;
    }

    public void setOr(Boolean or) {
        isOr = or;
    }
}
