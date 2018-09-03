package fr.nantes.eni.alterplanning.dao.mysql.entity;

import fr.nantes.eni.alterplanning.dao.mysql.entity.composite_keys.CalendarCoursPK;
import javax.persistence.*;

/**
 * Created by ughostephan on 23/06/2017.
 */
@Entity
@Table(name = "calendar_cours")
@IdClass(CalendarCoursPK.class)
public class CalendarCoursEntity {

    @Id
    @Column(name = "calendarId")
    private int calendarId;

    @Id
    @Column(name = "coursId")
    private String coursId;

    @Column(name = "isIndependantModule")
    private boolean isIndependantModule = false;

    public int getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(int calendarId) {
        this.calendarId = calendarId;
    }

    public String getCoursId() {
        return coursId;
    }

    public void setCoursId(String coursId) {
        this.coursId = coursId;
    }

    public boolean isIndependantModule() {
        return isIndependantModule;
    }

    public void setIndependantModule(boolean independantModule) {
        isIndependantModule = independantModule;
    }
}

