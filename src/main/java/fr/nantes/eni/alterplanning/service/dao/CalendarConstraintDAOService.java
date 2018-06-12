package fr.nantes.eni.alterplanning.service.dao;

import fr.nantes.eni.alterplanning.dao.mysql.entity.CalendarConstraintEntity;
import fr.nantes.eni.alterplanning.dao.mysql.repository.ICalendarConstraintRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by ughostephan on 23/06/2017.
 */
@Service
public class CalendarConstraintDAOService {

    @Resource
    private ICalendarConstraintRepository repository;

    public List<CalendarConstraintEntity> findByCalendarId(final int idCalendar) {
        return repository.findAllByCalendar(idCalendar);
    }

    public CalendarConstraintEntity create(final CalendarConstraintEntity calendarConstraintEntity) {
        return repository.save(calendarConstraintEntity);
    }

    public List<CalendarConstraintEntity> createAll(final List<CalendarConstraintEntity> entities) {
        return StreamSupport.stream(repository.saveAll(entities).spliterator(), false)
                .collect(Collectors.toList());
    }

    public void delete(final int id) {
        repository.deleteById(id);
    }
}
