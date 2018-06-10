package fr.nantes.eni.alterplanning.dao.mysql.entity.composite_keys;

import java.io.Serializable;

public class CalendarCoursPK implements Serializable {
    private int calendarId;
    private String coursId;

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
}
