package fr.nantes.eni.alterplanning.util;

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
}
