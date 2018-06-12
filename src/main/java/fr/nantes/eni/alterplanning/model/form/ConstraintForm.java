package fr.nantes.eni.alterplanning.model.form;

import fr.nantes.eni.alterplanning.dao.mysql.entity.enums.ConstraintType;

import javax.validation.constraints.NotNull;

public class ConstraintForm {

    @NotNull
    private ConstraintType type;

    @NotNull
    private String value;

    public ConstraintType getType() {
        return type;
    }

    public void setType(ConstraintType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
