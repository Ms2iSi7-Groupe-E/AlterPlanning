package fr.nantes.eni.alterplanning.service.dao;

import fr.nantes.eni.alterplanning.dao.mysql.entity.CalendarCoursEntity;
import fr.nantes.eni.alterplanning.dao.mysql.entity.CalendarEntity;
import fr.nantes.eni.alterplanning.dao.mysql.repository.ICalendarConstraintRepository;
import fr.nantes.eni.alterplanning.dao.mysql.repository.ICalendarCoursRepository;
import fr.nantes.eni.alterplanning.dao.mysql.repository.ICalendarRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by ughostephan on 23/06/2017.
 */
@Service
public class CalendarDAOService {

    @Resource
    private ICalendarRepository calendarRepository;

    @Resource
    private ICalendarCoursRepository calendarCoursRepository;

    @Resource
    private ICalendarConstraintRepository calendarConstraintRepository;

    public List<CalendarEntity> findAll() {
        return StreamSupport.stream(calendarRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public List<CalendarEntity> findAllOrderByDate() {
        return calendarRepository.findAllOrderByDate();
    }


    public CalendarEntity findById(final Integer id) {
        return calendarRepository.findById(id).orElse(null);
    }

    public CalendarEntity create(final CalendarEntity calendar) {
        return calendarRepository.save(calendar);
    }

    public void update(final CalendarEntity calendar) {
        CalendarEntity entity = calendarRepository.findById(calendar.getId()).orElse(null);

        if (entity == null)
            throw new EntityNotFoundException();

        calendarRepository.save(calendar);
    }

    public void delete(final Integer id) {
        calendarRepository.deleteById(id);
    }

    public boolean existById(final Integer id) {
        return calendarRepository.existsById(id);
    }

    @Transactional("mysqlTransactionManager")
    public void deleteCalendarAndCoursAndConstraints(final Integer id) {
        calendarCoursRepository.deleteByCalendar(id);
        calendarConstraintRepository.deleteByCalendar(id);
        calendarRepository.deleteById(id);
    }

    @Transactional("mysqlTransactionManager")
    public void addAndReplaceCoursWithUpdate(final List<CalendarCoursEntity> calendarCoursToAdd, final CalendarEntity cal) {
        calendarCoursRepository.deleteByCalendar(cal.getId());
        calendarCoursRepository.saveAll(calendarCoursToAdd);
        calendarRepository.save(cal);
    }
}
