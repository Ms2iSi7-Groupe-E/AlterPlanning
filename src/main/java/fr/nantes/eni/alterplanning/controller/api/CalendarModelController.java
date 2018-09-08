package fr.nantes.eni.alterplanning.controller.api;

import fr.nantes.eni.alterplanning.dao.mysql.entity.CalendarConstraintEntity;
import fr.nantes.eni.alterplanning.dao.mysql.entity.CalendarCoursEntity;
import fr.nantes.eni.alterplanning.dao.mysql.entity.CalendarEntity;
import fr.nantes.eni.alterplanning.dao.mysql.entity.CalendarModelEntity;
import fr.nantes.eni.alterplanning.dao.mysql.entity.enums.CalendarState;
import fr.nantes.eni.alterplanning.exception.RestResponseException;
import fr.nantes.eni.alterplanning.model.form.AddCalendarModelForm;
import fr.nantes.eni.alterplanning.model.response.StringResponse;
import fr.nantes.eni.alterplanning.service.dao.CalendarConstraintDAOService;
import fr.nantes.eni.alterplanning.service.dao.CalendarCoursDAOService;
import fr.nantes.eni.alterplanning.service.dao.CalendarDAOService;
import fr.nantes.eni.alterplanning.service.dao.CalendarModelsDAOService;
import fr.nantes.eni.alterplanning.util.HistoryUtil;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
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
    private CalendarConstraintDAOService calendarConstraintDAOService;

    @Resource
    private CalendarCoursDAOService calendarCoursDAOService;

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

    @GetMapping("/{idCalendarModel}/duplicate")
    public CalendarEntity duplicateCalendarModel(@PathVariable(name = "idCalendarModel") int id) throws RestResponseException {
        // Find Calendar to delete
        final CalendarModelEntity c = calendarModelsDAOService.findById(id);

        if (c == null) {
            throw new RestResponseException(HttpStatus.NOT_FOUND, "Modèle de calendrier non trouvé");
        }

        final CalendarEntity calendarToDuplicate = calendarDAOService.findById(c.getCalendarId());
        final List<CalendarConstraintEntity> calendarConstraintsToDuplicate = calendarConstraintDAOService.findByCalendarId(c.getCalendarId());
        final List<CalendarCoursEntity> calendarCoursToDuplicate = calendarCoursDAOService.findByCalendarId(c.getCalendarId());

        final CalendarEntity calendarToCreate = new CalendarEntity();
        calendarToCreate.setState(CalendarState.PROPOSAL);
        calendarToCreate.setStartDate(calendarToDuplicate.getStartDate());
        calendarToCreate.setEndDate(calendarToDuplicate.getEndDate());
        calendarToCreate.setEntrepriseId(calendarToDuplicate.getEntrepriseId());
        calendarToCreate.setStagiaireId(calendarToDuplicate.getStagiaireId());

        final CalendarEntity calendarCreated = calendarDAOService.create(calendarToCreate);

        final List<CalendarConstraintEntity> calendarConstraintsToCreate = calendarConstraintsToDuplicate
                .stream().map(cc -> {
                    final CalendarConstraintEntity ccToCreate = new CalendarConstraintEntity();
                    ccToCreate.setCalendarId(calendarCreated.getId());
                    ccToCreate.setConstraintType(cc.getConstraintType());
                    ccToCreate.setConstraintValue(cc.getConstraintValue());
                    return ccToCreate;
                }).collect(Collectors.toList());

        final List<CalendarCoursEntity> calendarCoursToCreate = calendarCoursToDuplicate
                .stream().map(cc -> {
                    final CalendarCoursEntity ccToCreate = new CalendarCoursEntity();
                    ccToCreate.setCalendarId(calendarCreated.getId());
                    ccToCreate.setCoursId(cc.getCoursId());
                    ccToCreate.setIndependantModule(cc.isIndependantModule());
                    return ccToCreate;
                }).collect(Collectors.toList());

        calendarConstraintDAOService.createAll(calendarConstraintsToCreate);
        calendarCoursDAOService.createAll(calendarCoursToCreate);

        // Ajout du calendrier n°41
        historyUtil.addLine("Ajout du calendrier n°" + calendarCreated.getId() +
                " (A partir d'un modèle)");

        return calendarCreated;
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
