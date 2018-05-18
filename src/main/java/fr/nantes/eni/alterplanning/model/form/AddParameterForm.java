package fr.nantes.eni.alterplanning.model.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class AddParameterForm implements Serializable {

    @NotBlank
    @Size(min = 3, max = 50)
    private String key;

    @NotBlank
    private String value;


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
