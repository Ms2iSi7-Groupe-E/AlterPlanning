package fr.nantes.eni.alterplanning.model.form;

import java.io.Serializable;

public class UpdateStagiaireForm implements Serializable {

    private Integer stagiaireId;

    public Integer getStagiaireId() {
        return stagiaireId;
    }

    public void setStagiaireId(Integer stagiaireId) {
        this.stagiaireId = stagiaireId;
    }
}
