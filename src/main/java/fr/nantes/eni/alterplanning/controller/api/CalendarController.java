package fr.nantes.eni.alterplanning.controller.api;

import fr.nantes.eni.alterplanning.dao.mysql.entity.CalendarConstraintEntity;
import fr.nantes.eni.alterplanning.dao.mysql.entity.CalendarCoursEntity;
import fr.nantes.eni.alterplanning.dao.mysql.entity.CalendarEntity;
import fr.nantes.eni.alterplanning.dao.mysql.entity.enums.CalendarState;
import fr.nantes.eni.alterplanning.dao.sqlserver.entity.CoursEntity;
import fr.nantes.eni.alterplanning.dao.sqlserver.entity.EntrepriseEntity;
import fr.nantes.eni.alterplanning.dao.sqlserver.entity.StagiaireEntity;
import fr.nantes.eni.alterplanning.exception.RestResponseException;
import fr.nantes.eni.alterplanning.model.form.AddCalendarForm;
import fr.nantes.eni.alterplanning.model.response.CalendarDetailResponse;
import fr.nantes.eni.alterplanning.model.response.CalendarResponse;
import fr.nantes.eni.alterplanning.model.response.StringResponse;
import fr.nantes.eni.alterplanning.service.dao.*;
import fr.nantes.eni.alterplanning.util.MediaTypeUtil;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.validation.Valid;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ughostephan on 23/06/2017.
 */
@RestController
@RequestMapping("/api/calendar")
public class CalendarController {

    @Resource
    private ServletContext servletContext;

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

        return calendarDAOService.findAll().stream().map(c -> {
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
            throw new RestResponseException(HttpStatus.NOT_FOUND, "Calendar not found");
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
            final List<String> idsCours = calendarCoursEntities.stream().map(CalendarCoursEntity::getCoursId).collect(Collectors.toList());
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
            result.addError(new FieldError("startDate",  "startDate", "should be before endDate"));
            result.addError(new FieldError("endDate",  "endDate", "should be after startDate"));
        }

        boolean stagaireOrEntrepriseError = false;

        if (form.getStagiaireId() != null && !stagiaireDAOService.existsById(form.getStagiaireId())) {
            result.addError(new FieldError("stagiaireId",  "stagiaireId", "not exist"));
            stagaireOrEntrepriseError = true;
        }

        if (form.getEntrepriseId() != null && !entrepriseDAOService.existsById(form.getEntrepriseId())) {
            result.addError(new FieldError("entrepriseId",  "entrepriseId", "not exist"));
            stagaireOrEntrepriseError = true;
        }

        if (form.getStagiaireId() != null && form.getEntrepriseId() != null && !stagaireOrEntrepriseError) {
            final List<EntrepriseEntity> stagiaireEntreprises = entrepriseDAOService.findByStagiaire(form.getStagiaireId());
            final List<Integer> entrepriseIds = stagiaireEntreprises.stream()
                    .map(EntrepriseEntity::getCodeEntreprise)
                    .collect(Collectors.toList());

            if (stagiaireEntreprises.isEmpty() || !entrepriseIds.contains(form.getEntrepriseId())) {
                result.addError(new FieldError("entrepriseId",  "entrepriseId", "not the stagiaire entreprise"));
            }
        }

        // TODO : Vérifier contraintes d'un calendrier

        if (result.hasErrors()) {
            throw new RestResponseException(HttpStatus.BAD_REQUEST, "Bad request", result);
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

        return getCalendarDetailsById(createdCalendar.getId());
    }

    @PutMapping("/{idCalendar}")
    public StringResponse updateCalendar(@PathVariable(name = "idCalendar") int id) throws RestResponseException {
        // TODO : modif de calendar
        throw new RestResponseException(HttpStatus.NOT_IMPLEMENTED, "Not yet implemented");
    }

    @DeleteMapping("/{idCalendar}")
    public StringResponse deleteCalendar(@PathVariable(name = "idCalendar") int id) throws RestResponseException {
        // Find Calendar to delete
        final CalendarEntity c = calendarDAOService.findById(id);

        if (c == null) {
            throw new RestResponseException(HttpStatus.NOT_FOUND, "Calendar not found");
        }

        // Before delete calendar
        // TODO : delete cours associés
        // TODO : delete contraintes associés
        // Delete Calendar
        calendarDAOService.delete(id);

        return new StringResponse("Calendar successfully deleted");
    }

    @GetMapping("/{idCalendar}/download")
    public ResponseEntity<InputStreamResource> downloadCalendar(@PathVariable(name = "idCalendar") int id) throws RestResponseException {
        // Find Calendar to delete
        final CalendarEntity c = calendarDAOService.findById(id);

        if (c == null) {
            throw new RestResponseException(HttpStatus.NOT_FOUND, "Calendar not found");
        }

        // TODO code for download PDF of a calendar
        final String str = "Hello World";

        File file;
        InputStreamResource resource;

        try {
            //create a temp file
            file = File.createTempFile("test", ".txt");
            // Write in file
            FileWriter writer = new FileWriter(file);
            writer.write(str);
            writer.close();
            //create stream
            resource = new InputStreamResource(new FileInputStream(file));
            System.out.println(file.getName());
        } catch (IOException e){
            e.printStackTrace();
            throw new RestResponseException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        final String filename = file.getName();
        final MediaType mediaType = MediaTypeUtil.getMediaTypeForFileName(servletContext, filename);

        return ResponseEntity.ok()
                // Content-Disposition
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + filename)
                // Content-Type
                .contentType(mediaType)
                // Contet-Length
                .contentLength(file.length())
                .body(resource);
    }

    @PostMapping("/{idCalendar}/cours")
    @ResponseStatus(HttpStatus.CREATED)
    public StringResponse addCoursToCalendar(@PathVariable(name = "idCalendar") int id) throws RestResponseException {
        // TODO : ajout d'un cour
        throw new RestResponseException(HttpStatus.NOT_IMPLEMENTED, "Not yet implemented");
    }

    @DeleteMapping("/{idCalendar}/cours/{idCours}")
    public StringResponse deleteCoursForCalendar(@PathVariable(name = "idCalendar") int idCalendar,
                                                 @PathVariable(name = "idCours") String idCours) throws RestResponseException {
        // TODO : suppression d'un cours
        throw new RestResponseException(HttpStatus.NOT_IMPLEMENTED, "Not yet implemented");
    }

}
