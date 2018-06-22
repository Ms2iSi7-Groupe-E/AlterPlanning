package fr.nantes.eni.alterplanning.dao.mysql.entity.composite_keys;

import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CalendarCoursPK that = (CalendarCoursPK) o;
        return calendarId == that.calendarId &&
                Objects.equals(coursId, that.coursId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(calendarId, coursId);
    }
}
