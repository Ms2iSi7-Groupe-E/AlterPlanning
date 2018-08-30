package fr.nantes.eni.alterplanning.util;

import fr.nantes.eni.alterplanning.dao.mysql.entity.CalendarEntity;
import fr.nantes.eni.alterplanning.dao.sqlserver.entity.LieuEntity;
import fr.nantes.eni.alterplanning.model.simplebean.CoursComplet;
import fr.nantes.eni.alterplanning.model.simplebean.LineCalendarGeneration;
import fr.nantes.eni.alterplanning.service.dao.LieuDAOService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Component
public class CalendarExportUtil {

    @Resource
    private LieuDAOService lieuDAOService;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat(AlterDateUtil.ddMMyyyyWithSlash);

    public List<LineCalendarGeneration> getCalendarLines(final CalendarEntity calendar, final List<CoursComplet> coursComplets) {
        coursComplets.sort(Comparator.comparing(CoursComplet::getDebut));
        final List<LieuEntity> lieux = lieuDAOService.findAll();
        final List<LineCalendarGeneration> lines = new ArrayList<>();
        final Date startDateFirstCourse = coursComplets.get(0).getDebut();
        final Date endDateLastCourse = coursComplets.get(coursComplets.size() - 1).getFin();

        if (calendar.getStartDate() != null && calendar.getStartDate().before(startDateFirstCourse)) {
            final Date prevFriday = AlterDateUtil.prevFriday(startDateFirstCourse);
            final LineCalendarGeneration entreprisePeriode = new LineCalendarGeneration();
            entreprisePeriode.setDebut(dateFormat.format(calendar.getStartDate()));
            entreprisePeriode.setFin(dateFormat.format(prevFriday));
            entreprisePeriode.setEntreprisePeriode(true);
            lines.add(entreprisePeriode);
        }

        for (int i = 0; i < coursComplets.size(); i++) {
            final CoursComplet co = coursComplets.get(i);
            LineCalendarGeneration li = new LineCalendarGeneration();
            li.setLibelle(co.getLibelleModule());
            li.setLieu(lieux.stream().filter(l -> l.getCodeLieu().equals(co.getCodeLieu()))
                    .findFirst().orElse(null));
            li.setDureeReelleEnHeures(co.getDureeReelleEnHeures());
            li.setDebut(dateFormat.format(co.getDebut()));
            li.setFin(dateFormat.format(co.getFin()));
            lines.add(li);

            if (i < coursComplets.size() - 1) {
                final CoursComplet nextCours = coursComplets.get(i + 1);
                final Date nextMonday = AlterDateUtil.nextMonday(co.getFin());

                if (!AlterDateUtil.inSameWeek(nextCours.getDebut(), nextMonday)) {
                    final Date prevFriday = AlterDateUtil.prevFriday(nextCours.getDebut());
                    final LineCalendarGeneration entreprisePeriode = new LineCalendarGeneration();
                    entreprisePeriode.setDebut(dateFormat.format(nextMonday));
                    entreprisePeriode.setFin(dateFormat.format(prevFriday));
                    entreprisePeriode.setEntreprisePeriode(true);
                    lines.add(entreprisePeriode);
                }
            }
        }

        if (calendar.getEndDate() != null && calendar.getEndDate().after(endDateLastCourse)) {
            final Date nextMonday = AlterDateUtil.nextMonday(endDateLastCourse);
            final LineCalendarGeneration entreprisePeriode = new LineCalendarGeneration();
            entreprisePeriode.setDebut(dateFormat.format(nextMonday));
            entreprisePeriode.setFin(dateFormat.format(calendar.getEndDate()));
            entreprisePeriode.setEntreprisePeriode(true);
            lines.add(entreprisePeriode);
        }

        return lines;
    }
}
