package fr.nantes.eni.alterplanning.util;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;

public class AlterDateUtil {

    private AlterDateUtil() { }

    public static final String timezone = "Europe/Paris";

    public static final String yyyyMMdd = "yyyy-MM-dd";

    public static final String ddMMyyyy = "dd-MM-yyyy";

    public static final String ddMMyyyyWithSlash = "dd/MM/yyyy";

    public static final String yyyyMMddHHmmss = "yyyy-MM-dd HH:mm:ss";

    public static final String ddMMyyyyHHmmss = "dd-MM-yyyy HH:mm:ss";

    public static boolean isIncludeInPeriode(Date start, Date end, Date toCheck) {
        if (toCheck == null) {
            return false;
        }

        if (start != null && end != null) {
            return (start.before(toCheck) || start.equals(toCheck)) && (end.after(toCheck) || end.equals(toCheck));
        } else if (start != null) {
            return start.before(toCheck) || start.equals(toCheck);
        } else if (end != null) {
            return end.after(toCheck) || end.equals(toCheck);
        }

        return false;
    }

    public static Date nextMonday(final Date date) {
        LocalDate ld = Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        ld = ld.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        return Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static Date prevFriday(final Date date) {
        LocalDate ld = Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        ld = ld.with(TemporalAdjusters.previous(DayOfWeek.FRIDAY));
        return Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static boolean inSameWeek(final Date d1, final Date d2) {
        final Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(d1.getTime());
        final int year1 = c1.get(Calendar.YEAR);
        final int week1 = c1.get(Calendar.WEEK_OF_YEAR);

        final Calendar c2 = Calendar.getInstance();
        c2.setTimeInMillis(d2.getTime());
        final int year2 = c2.get(Calendar.YEAR);
        final int week2 = c2.get(Calendar.WEEK_OF_YEAR);

        return year1 == year2 && week1 == week2;
    }
}
