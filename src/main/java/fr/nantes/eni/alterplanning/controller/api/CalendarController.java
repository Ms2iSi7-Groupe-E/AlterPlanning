package fr.nantes.eni.alterplanning.controller.api;

import fr.nantes.eni.alterplanning.dao.mysql.entity.CalendarConstraintEntity;
import fr.nantes.eni.alterplanning.dao.mysql.entity.CalendarCoursEntity;
import fr.nantes.eni.alterplanning.dao.mysql.entity.CalendarEntity;
import fr.nantes.eni.alterplanning.dao.mysql.entity.enums.CalendarState;
import fr.nantes.eni.alterplanning.dao.sqlserver.entity.CoursEntity;
import fr.nantes.eni.alterplanning.dao.sqlserver.entity.EntrepriseEntity;
import fr.nantes.eni.alterplanning.dao.sqlserver.entity.StagiaireEntity;
import fr.nantes.eni.alterplanning.exception.RestResponseException;
import fr.nantes.eni.alterplanning.model.form.AddCalendarCoursForm;
import fr.nantes.eni.alterplanning.model.form.AddCalendarForm;
import fr.nantes.eni.alterplanning.model.response.CalendarDetailResponse;
import fr.nantes.eni.alterplanning.model.response.CalendarResponse;
import fr.nantes.eni.alterplanning.model.response.StringResponse;
import fr.nantes.eni.alterplanning.service.dao.*;
import fr.nantes.eni.alterplanning.util.HistoryUtil;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * Created by ughostephan on 23/06/2017.
 */
@RestController
@RequestMapping("/api/calendar")
public class CalendarController {

    @Resource
    private HistoryUtil historyUtil;

    @Resource
    private CalendarDAOService calendarDAOService;

    @Resource
    private CalendarCoursDAOService calendarCoursDAOService;

    @Resource
    private CalendarConstraintDAOService calendarConstraintDAOService;

    @Resource
    private CoursDAOService coursDAOService;

    @Resource
    private StagiaireDAOService stagiaireDAOService;

    @Resource
    private EntrepriseDAOService entrepriseDAOService;

    @GetMapping("")
    public List<CalendarResponse> getCalendars() {
        final List<StagiaireEntity> allStagiaires = stagiaireDAOService.findAll();
        final List<EntrepriseEntity> allEntreprises = entrepriseDAOService.findAll();

        return calendarDAOService.findAllOrderByDate().stream().map(c -> {
            final CalendarResponse calendar = new CalendarResponse();
            calendar.setId(c.getId());
            calendar.setStartDate(c.getStartDate());
            calendar.setEndDate(c.getEndDate());
            calendar.setCreatedAt(c.getCreatedAt());
            calendar.setModel(c.getModel());
            calendar.setState(c.getState());

            if (c.getEntrepriseId() != null) {
                final EntrepriseEntity entrepriseEntity = allEntreprises
                        .stream()
                        .filter(x -> x.getCodeEntreprise().equals(c.getEntrepriseId()))
                        .findFirst()
                        .orElse(null);
                calendar.setEntreprise(entrepriseEntity);
            }

            if (c.getStagiaireId() != null) {
                final StagiaireEntity stagiaireEntity = allStagiaires
                        .stream()
                        .filter(x -> x.getCodeStagiaire().equals(c.getStagiaireId()))
                        .findFirst()
                        .orElse(null);
                calendar.setStagiaire(stagiaireEntity);
            }

            return calendar;
        }).collect(Collectors.toList());
    }

    @GetMapping("/{idCalendar}")
    public CalendarDetailResponse getCalendarDetailsById(@PathVariable(name = "idCalendar") int id) throws RestResponseException {
        final CalendarEntity c = calendarDAOService.findById(id);

        if (c == null) {
            throw new RestResponseException(HttpStatus.NOT_FOUND, "Calendrier non trouvé");
        }

        final CalendarDetailResponse calendarDetailResponse = new CalendarDetailResponse();
        calendarDetailResponse.setId(c.getId());
        calendarDetailResponse.setStartDate(c.getStartDate());
        calendarDetailResponse.setEndDate(c.getEndDate());
        calendarDetailResponse.setCreatedAt(c.getCreatedAt());
        calendarDetailResponse.setModel(c.getModel());
        calendarDetailResponse.setState(c.getState());

        if (c.getEntrepriseId() != null) {
            EntrepriseEntity entrepriseEntity = entrepriseDAOService.findById(c.getEntrepriseId());
            calendarDetailResponse.setEntreprise(entrepriseEntity);
        }

        if (c.getStagiaireId() != null) {
            StagiaireEntity stagiaireEntity = stagiaireDAOService.findById(c.getStagiaireId());
            calendarDetailResponse.setStagiaire(stagiaireEntity);
        }

        final List<CalendarCoursEntity> calendarCoursEntities = calendarCoursDAOService.findByCalendarId(id);

        if (!calendarCoursEntities.isEmpty()) {
            final List<String> idsCours = calendarCoursEntities
                    .stream()
                    .map(CalendarCoursEntity::getCoursId)
                    .distinct()
                    .collect(Collectors.toList());
            final List<CoursEntity> coursEntities = coursDAOService.findByListIdCours(idsCours);

            calendarDetailResponse.setCours(coursEntities);
        }

        final List<CalendarConstraintEntity> constraints = calendarConstraintDAOService.findByCalendarId(id);

        if (!constraints.isEmpty()) {
            calendarDetailResponse.setConstraints(constraints);
        }

        return calendarDetailResponse;
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public CalendarDetailResponse addCalendar(@Valid @RequestBody AddCalendarForm form, BindingResult result) throws RestResponseException {

        if (form.getEndDate() != null && form.getStartDate() != null && form.getEndDate().before(form.getStartDate())) {
            result.addError(new FieldError("startDate",  "startDate", "doit être avant la date de fin"));
            result.addError(new FieldError("endDate",  "endDate", "doit être après la date de début"));
        }

        boolean stagaireOrEntrepriseError = false;

        if (form.getStagiaireId() != null && !stagiaireDAOService.existsById(form.getStagiaireId())) {
            result.addError(new FieldError("stagiaireId",  "stagiaireId", "n'existe pas en base"));
            stagaireOrEntrepriseError = true;
        }

        if (form.getEntrepriseId() != null && !entrepriseDAOService.existsById(form.getEntrepriseId())) {
            result.addError(new FieldError("entrepriseId",  "entrepriseId", "n'existe pas en base"));
            stagaireOrEntrepriseError = true;
        }

        if (form.getStagiaireId() != null && form.getEntrepriseId() != null && !stagaireOrEntrepriseError) {
            final List<EntrepriseEntity> stagiaireEntreprises = entrepriseDAOService.findByStagiaire(form.getStagiaireId());
            final List<Integer> entrepriseIds = stagiaireEntreprises.stream()
                    .map(EntrepriseEntity::getCodeEntreprise)
                    .collect(Collectors.toList());

            if (stagiaireEntreprises.isEmpty() || !entrepriseIds.contains(form.getEntrepriseId())) {
                result.addError(new FieldError("entrepriseId",  "entrepriseId", "l'entreprise ne correspond pas à celle du stagiaire selectionné"));
            }
        }

        // TODO : Vérifier contraintes d'un calendrier

        if (result.hasErrors()) {
            throw new RestResponseException(HttpStatus.BAD_REQUEST, "Erreur au niveau des champs", result);
        }

        // Define new Calendar
        final CalendarEntity calendar = new CalendarEntity();
        calendar.setStagiaireId(form.getStagiaireId());
        calendar.setEntrepriseId(form.getEntrepriseId());
        calendar.setStartDate(form.getStartDate());
        calendar.setEndDate(form.getEndDate());
        calendar.setState(CalendarState.DRAFT);

        final CalendarEntity createdCalendar = calendarDAOService.create(calendar);

        // Ajouter les contraintes d'un calendrier en base
        if (!form.getConstraints().isEmpty()) {
            List<CalendarConstraintEntity> constraints = form.getConstraints().stream().map(c -> {
                final CalendarConstraintEntity calendarConstraintEntity = new CalendarConstraintEntity();
                calendarConstraintEntity.setCalendarId(createdCalendar.getId());
                calendarConstraintEntity.setConstraintType(c.getType());
                calendarConstraintEntity.setConstraintValue(c.getValue());
                return calendarConstraintEntity;
            }).collect(Collectors.toList());
            calendarConstraintDAOService.createAll(constraints);
        }

        historyUtil.addLine("Ajout du calendrier n°" + createdCalendar.getId());

        return getCalendarDetailsById(createdCalendar.getId());
    }

    @PutMapping("/{idCalendar}")
    public StringResponse updateCalendar(@PathVariable(name = "idCalendar") int id) throws RestResponseException {
        // TODO : modif de calendar
        throw new RestResponseException(HttpStatus.NOT_IMPLEMENTED, "Not yet implemented");

        //historyUtil.addLine("Modification du calendrier n°" + id);
    }

    @DeleteMapping("/{idCalendar}")
    public StringResponse deleteCalendar(@PathVariable(name = "idCalendar") int id) throws RestResponseException {
        // Find Calendar to delete
        final CalendarEntity c = calendarDAOService.findById(id);

        if (c == null) {
            throw new RestResponseException(HttpStatus.NOT_FOUND, "Calendrier non trouvé");
        }

        if (c.getModel()) {
            throw new RestResponseException(HttpStatus.CONFLICT, "Impossible de supprimer ce calendrier, car celui ci est utilisé en tant que modèle");
        }

        // Before delete calendar
        // TODO : delete cours associés
        // TODO : delete contraintes associés
        // Delete Calendar

        calendarDAOService.delete(id);

        historyUtil.addLine("Suppression du calendrier n°" + id);

        return new StringResponse("Calendrier supprimé avec succès");
    }

    @GetMapping("/{idCalendar}/cours-for-generate-calendar")
    public List<CoursEntity> getCoursForCalendarInGeneration(@PathVariable(name = "idCalendar") int id) throws RestResponseException {
        // Find Calendar
        final CalendarEntity c = calendarDAOService.findById(id);

        if (c == null) {
            throw new RestResponseException(HttpStatus.NOT_FOUND, "Calendrier non trouvé");
        }

        if (c.getState() != CalendarState.DRAFT) {
            throw new RestResponseException(HttpStatus.CONFLICT, "Le calendrier doit être à l'état de brouillon");
        }

        // TODO

        throw new RestResponseException(HttpStatus.NOT_IMPLEMENTED, "Not yet implemented");
    }

    @PostMapping("/{idCalendar}/cours")
    @ResponseStatus(HttpStatus.CREATED)
    public StringResponse addCoursToCalendar(@Valid @RequestBody AddCalendarCoursForm form, BindingResult result,
                                             @PathVariable(name = "idCalendar") int id) throws RestResponseException {
        // Find Calendar
        final CalendarEntity cal = calendarDAOService.findById(id);

        if (cal == null) {
            throw new RestResponseException(HttpStatus.NOT_FOUND, "Calendrier non trouvé");
        }

        if (cal.getState() != CalendarState.DRAFT) {
            throw new RestResponseException(HttpStatus.CONFLICT, "Le calendrier doit être à l'état de brouillon");
        }

        if (result.hasErrors()) {
            throw new RestResponseException(HttpStatus.BAD_REQUEST, "Erreur au niveau des champs", result);
        }

        // Enlever les ids en doublon
        final List<String> coursIds = form.getCoursIds().stream().distinct().collect(Collectors.toList());

        // Vérifier que les ids correspondent en base
        final StringJoiner errorIds = new StringJoiner(", ");
        coursIds.forEach(c -> {
            if (!coursDAOService.existsById(c)) {
                errorIds.add(c);
            }
        });

        if (errorIds.length() != 0) {
            result.addError(new FieldError("coursIds", "coursIds",
                    "Les cours suivant <" + errorIds.toString() + "> n'existent pas."));
        }

        if (result.hasErrors()) {
            throw new RestResponseException(HttpStatus.BAD_REQUEST, "Erreur au niveau des champs", result);
        }

        final List<CalendarCoursEntity> calendarCoursEntities = coursIds.stream().map(c -> {
            final CalendarCoursEntity calendarCoursEntity = new CalendarCoursEntity();
            calendarCoursEntity.setCalendarId(cal.getId());
            calendarCoursEntity.setCoursId(c);
            return calendarCoursEntity;
        }).collect(Collectors.toList());

        calendarCoursDAOService.createAll(calendarCoursEntities);
        cal.setState(CalendarState.PROPOSAL);
        calendarDAOService.update(cal);

        return new StringResponse("Les cours ont bien été ajoutés pour ce calendrier");
    }

    @DeleteMapping("/{idCalendar}/cours/{idCours}")
    public StringResponse deleteCoursForCalendar(@PathVariable(name = "idCalendar") int idCalendar,
                                                 @PathVariable(name = "idCours") String idCours) throws RestResponseException {
        // TODO : suppression d'un cours
        throw new RestResponseException(HttpStatus.NOT_IMPLEMENTED, "Not yet implemented");
    }

}
