package fr.nantes.eni.alterplanning.dao.mysql.entity;

import fr.nantes.eni.alterplanning.dao.mysql.entity.enums.ConstraintType;
import javax.persistence.*;

/**
 * Created by ughostephan on 23/06/2017.
 */
@Entity
@Table(name = "calendar_constraints")
public class CalendarConstraintEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "calendarId")
    private int calendarId;

    @Column(name = "constraintType")
    @Enumerated(EnumType.STRING)
    private ConstraintType constraintType;

    @Column(name = "constraintValue")
    private String constraintValue;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(int calendarId) {
        this.calendarId = calendarId;
    }

    public ConstraintType getConstraintType() {
        return constraintType;
    }

    public void setConstraintType(ConstraintType constraintType) {
        this.constraintType = constraintType;
    }

    public String getConstraintValue() {
        return constraintValue;
    }

    public void setConstraintValue(String constraintValue) {
        this.constraintValue = constraintValue;
    }
}

