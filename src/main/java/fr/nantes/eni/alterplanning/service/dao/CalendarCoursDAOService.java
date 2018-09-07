package fr.nantes.eni.alterplanning.service.dao;

import fr.nantes.eni.alterplanning.dao.mysql.entity.CalendarCoursEntity;
import fr.nantes.eni.alterplanning.dao.mysql.entity.CalendarEntity;
import fr.nantes.eni.alterplanning.dao.mysql.entity.composite_keys.CalendarCoursPK;
import fr.nantes.eni.alterplanning.dao.mysql.repository.ICalendarCoursRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by ughostephan on 23/06/2017.
 */
@Service
public class CalendarCoursDAOService {

    @Resource
    private ICalendarCoursRepository repository;

    public List<CalendarCoursEntity> findByCalendarId(final int idCalendar) {
        return repository.findAllByCalendar(idCalendar);
    }

    public CalendarCoursEntity create(final CalendarCoursEntity calendarCours) {
        return repository.save(calendarCours);
    }

    public List<CalendarCoursEntity> createAll(final List<CalendarCoursEntity> entities) {
        return StreamSupport.stream(repository.saveAll(entities).spliterator(), false)
                .collect(Collectors.toList());
    }


    public void delete(final CalendarCoursPK pk) {
        repository.deleteById(pk);
    }

    public void deleteAllForCalendarId(int id) {
        repository.deleteByCalendar(id);
    }
}
