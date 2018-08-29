package fr.nantes.eni.alterplanning.dao.mysql.repository;

import fr.nantes.eni.alterplanning.dao.mysql.entity.CalendarModelEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ughostephan on 23/06/2017.
 */
@Repository
public interface ICalendarModelsRepository extends CrudRepository<CalendarModelEntity, Integer> {

    @SuppressWarnings("JpaQlInspection")
    @Query("SELECT c FROM CalendarModelEntity c " +
            "WHERE c.calendarId = :calendarId ")
    List<CalendarModelEntity> findAllByIdCalendar(@Param("calendarId") int idCalendar);
}
