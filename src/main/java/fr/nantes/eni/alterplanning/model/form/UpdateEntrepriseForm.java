package fr.nantes.eni.alterplanning.model.form;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class UpdateEntrepriseForm implements Serializable {

    private Integer entrepriseId;

    public Integer getEntrepriseId() {
        return entrepriseId;
    }

    public void setEntrepriseId(Integer entrepriseId) {
        this.entrepriseId = entrepriseId;
    }
}
