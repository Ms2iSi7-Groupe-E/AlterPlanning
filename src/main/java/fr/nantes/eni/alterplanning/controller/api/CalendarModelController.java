package fr.nantes.eni.alterplanning.controller.api;

import fr.nantes.eni.alterplanning.dao.mysql.entity.CalendarEntity;
import fr.nantes.eni.alterplanning.dao.mysql.entity.CalendarModelEntity;
import fr.nantes.eni.alterplanning.exception.RestResponseException;
import fr.nantes.eni.alterplanning.model.response.StringResponse;
import fr.nantes.eni.alterplanning.service.dao.CalendarDAOService;
import fr.nantes.eni.alterplanning.service.dao.CalendarModelsDAOService;
import fr.nantes.eni.alterplanning.util.HistoryUtil;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by ughostephan on 23/06/2017.
 */
@RestController
@RequestMapping("/api/calendar/model")
public class CalendarModelController {

    @Resource
    private CalendarModelsDAOService calendarModelsDAOService;

    @Resource
    private CalendarDAOService calendarDAOService;

    @Resource
    private HistoryUtil historyUtil;

    @GetMapping("")
    public List<CalendarModelEntity> getCalendarModel() {
        return calendarModelsDAOService.findAll();
    }

    @DeleteMapping("/{idCalendarModel}")
    public StringResponse deleteCalendarModel(@PathVariable(name = "idCalendarModel") int idCalendarModel) throws RestResponseException {
        // Find Calendar to delete
        final CalendarModelEntity c = calendarModelsDAOService.findById(idCalendarModel);

        if (c == null) {
            throw new RestResponseException(HttpStatus.NOT_FOUND, "Calendrier non trouvé");
        }

        int calendarId = c.getCalendarId();
        calendarModelsDAOService.delete(idCalendarModel);
        final List<CalendarModelEntity> calendarModelEntityList = calendarModelsDAOService.findAllByIdCalendar(calendarId);

        if (calendarModelEntityList.size() == 0) {
            final CalendarEntity calendarEntity = calendarDAOService.findById(calendarId);
            calendarEntity.setModel(false);
            calendarDAOService.update(calendarEntity);
        }

        historyUtil.addLine("Suppression du modèle de calendrier n°" + idCalendarModel + " (" +  c.getName() + ")");

        return new StringResponse("Modèle de calendrier supprimé avec succès");
    }
}
