package fr.nantes.eni.alterplanning.model.form;

import javax.validation.constraints.NotBlank;

public class UpdateParameterForm {

    @NotBlank
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
