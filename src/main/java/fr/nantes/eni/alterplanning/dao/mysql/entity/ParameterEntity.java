package fr.nantes.eni.alterplanning.dao.mysql.entity;

import javax.persistence.*;

@Entity
@Table(name = "parameters")
public class ParameterEntity {

    @Id
    @Column(name = "parameter_key")
    private String key;

    @Column(name = "parameter_value")
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




