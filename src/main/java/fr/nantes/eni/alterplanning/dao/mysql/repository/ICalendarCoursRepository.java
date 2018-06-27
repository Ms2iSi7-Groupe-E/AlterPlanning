package fr.nantes.eni.alterplanning.dao.mysql.repository;

import fr.nantes.eni.alterplanning.dao.mysql.entity.CalendarCoursEntity;
import fr.nantes.eni.alterplanning.dao.mysql.entity.UserEntity;
import fr.nantes.eni.alterplanning.dao.mysql.entity.composite_keys.CalendarCoursPK;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ughostephan on 23/06/2017.
 */
@Repository
public interface ICalendarCoursRepository extends CrudRepository<CalendarCoursEntity, CalendarCoursPK> {

    @SuppressWarnings("JpaQlInspection")
    @Query("SELECT c FROM CalendarCoursEntity c " +
            "WHERE c.calendarId = :calendarId ")
    List<CalendarCoursEntity> findAllByCalendar(@Param("calendarId") int calendarId);

    @SuppressWarnings("JpaQlInspection")
    @Modifying
    @Query("DELETE FROM CalendarCoursEntity c " +
            "WHERE c.calendarId = :calendarId ")
    void deleteByCalendar(@Param("calendarId") int calendarId);
}
