package fr.nantes.eni.alterplanning.controller.api;

import fr.nantes.eni.alterplanning.dao.mysql.entity.CalendarEntity;
import fr.nantes.eni.alterplanning.dao.mysql.entity.CalendarModelEntity;
import fr.nantes.eni.alterplanning.dao.sqlserver.entity.EntrepriseEntity;
import fr.nantes.eni.alterplanning.exception.RestResponseException;
import fr.nantes.eni.alterplanning.model.form.AddCalendarCoursForm;
import fr.nantes.eni.alterplanning.model.form.AddCalendarModelForm;
import fr.nantes.eni.alterplanning.model.form.DuplicateCalendarModelForm;
import fr.nantes.eni.alterplanning.model.response.StringResponse;
import fr.nantes.eni.alterplanning.service.dao.CalendarDAOService;
import fr.nantes.eni.alterplanning.service.dao.CalendarModelsDAOService;
import fr.nantes.eni.alterplanning.util.HistoryUtil;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ughostephan on 23/06/2017.
 */
@RestController
@RequestMapping("/api/calendar-model")
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

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public CalendarModelEntity createCalendarModel(@Valid @RequestBody AddCalendarModelForm form, BindingResult result)
            throws RestResponseException {

        if (form.getIdCalendar() != null && !calendarDAOService.existById(form.getIdCalendar())) {
            result.addError(new FieldError("idCalendar",  "idCalendar", "n'existe pas en base"));
        }

        if (result.hasErrors()) {
            throw new RestResponseException(HttpStatus.BAD_REQUEST, "Erreur au niveau des champs", result);
        }

        final CalendarModelEntity calendarModelEntity = new CalendarModelEntity();
        calendarModelEntity.setCalendarId(form.getIdCalendar());
        calendarModelEntity.setName(form.getName());
        calendarModelEntity.setCreatedAt(new Date());

        final CalendarEntity calendar = calendarDAOService.findById(form.getIdCalendar());
        calendar.setModel(true);
        calendarDAOService.update(calendar);

        final CalendarModelEntity createdModel = calendarModelsDAOService.create(calendarModelEntity);

        historyUtil.addLine("Ajout du modèle de calendrier n°" + createdModel.getId() +
                " (" +  createdModel.getName() + ")");

        return createdModel;
    }

    @PostMapping("/{idCalendarModel}")
    @ResponseStatus(HttpStatus.CREATED)
    public CalendarModelEntity duplicateCalendarModel(@Valid @RequestBody DuplicateCalendarModelForm form, BindingResult result,
                                                      @PathVariable(name = "idCalendarModel") int id) throws RestResponseException {
        // Find Calendar to delete
        final CalendarModelEntity c = calendarModelsDAOService.findById(id);

        if (c == null) {
            throw new RestResponseException(HttpStatus.NOT_FOUND, "Modèle de calendrier non trouvé");
        }

        if (result.hasErrors()) {
            throw new RestResponseException(HttpStatus.BAD_REQUEST, "Erreur au niveau des champs", result);
        }

        final CalendarModelEntity calendarToCreate = new CalendarModelEntity();
        calendarToCreate.setName(form.getName());
        calendarToCreate.setCalendarId(c.getCalendarId());
        calendarToCreate.setCreatedAt(new Date());

        final CalendarModelEntity duplicatedModel = calendarModelsDAOService.create(calendarToCreate);

        historyUtil.addLine("Ajout du modèle de calendrier n°" + duplicatedModel.getId() +
                " (" +  duplicatedModel.getName() + ") par duplication du modèle n°" + c.getId());

        return duplicatedModel;
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
