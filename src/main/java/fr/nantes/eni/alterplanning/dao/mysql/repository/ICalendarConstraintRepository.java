package fr.nantes.eni.alterplanning.dao.mysql.repository;

import fr.nantes.eni.alterplanning.dao.mysql.entity.CalendarConstraintEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ughostephan on 23/06/2017.
 */
@Repository
public interface ICalendarConstraintRepository extends CrudRepository<CalendarConstraintEntity, Integer> {

    @SuppressWarnings("JpaQlInspection")
    @Query("SELECT c FROM CalendarConstraintEntity c " +
            "WHERE c.calendarId = :calendarId ")
    List<CalendarConstraintEntity> findAllByCalendar(@Param("calendarId") int calendarId);
}
