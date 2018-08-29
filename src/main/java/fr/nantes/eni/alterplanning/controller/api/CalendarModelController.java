package fr.nantes.eni.alterplanning.controller.api;

import fr.nantes.eni.alterplanning.dao.mysql.entity.CalendarModelEntity;
import fr.nantes.eni.alterplanning.service.dao.CalendarModelsDAOService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by ughostephan on 23/06/2017.
 */
@RestController
@RequestMapping("/api/calendar/models")
public class CalendarModelController {

    @Resource
    private CalendarModelsDAOService calendarModelsDAOService;

    @GetMapping("")
    public List<CalendarModelEntity> getCalendarModel() {
        return calendarModelsDAOService.findAll();
    }
}
