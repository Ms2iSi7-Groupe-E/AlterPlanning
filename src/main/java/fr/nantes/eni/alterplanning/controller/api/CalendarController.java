package fr.nantes.eni.alterplanning.controller.api;

import fr.nantes.eni.alterplanning.dao.mysql.entity.CalendarConstraintEntity;
import fr.nantes.eni.alterplanning.dao.mysql.entity.CalendarCoursEntity;
import fr.nantes.eni.alterplanning.dao.mysql.entity.CalendarEntity;
import fr.nantes.eni.alterplanning.dao.mysql.entity.IndependantModuleEntity;
import fr.nantes.eni.alterplanning.dao.mysql.entity.enums.CalendarState;
import fr.nantes.eni.alterplanning.dao.mysql.entity.enums.ConstraintType;
import fr.nantes.eni.alterplanning.dao.sqlserver.entity.CoursEntity;
import fr.nantes.eni.alterplanning.dao.sqlserver.entity.EntrepriseEntity;
import fr.nantes.eni.alterplanning.dao.sqlserver.entity.StagiaireEntity;
import fr.nantes.eni.alterplanning.exception.RestResponseException;
import fr.nantes.eni.alterplanning.model.form.AddCalendarCoursForm;
import fr.nantes.eni.alterplanning.model.form.AddCalendarForm;
import fr.nantes.eni.alterplanning.model.form.StateForm;
import fr.nantes.eni.alterplanning.model.response.CalendarDetailResponse;
import fr.nantes.eni.alterplanning.model.response.CalendarResponse;
import fr.nantes.eni.alterplanning.model.response.CoursGenerationResponse;
import fr.nantes.eni.alterplanning.model.response.StringResponse;
import fr.nantes.eni.alterplanning.model.simplebean.CoursComplet;
import fr.nantes.eni.alterplanning.model.simplebean.ExcludeConstraint;
import fr.nantes.eni.alterplanning.model.simplebean.LineCalendarGeneration;
import fr.nantes.eni.alterplanning.service.dao.*;
import fr.nantes.eni.alterplanning.util.AlterDateUtil;
import fr.nantes.eni.alterplanning.util.CalendarExportUtil;
import fr.nantes.eni.alterplanning.util.HistoryUtil;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    private CalendarExportUtil calendarExportUtil;

    @Resource
    private StagiaireDAOService stagiaireDAOService;

    @Resource
    private EntrepriseDAOService entrepriseDAOService;

    @Resource
    private IndependantModuleDAOService independantModuleDAOService;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat(AlterDateUtil.ddMMyyyyWithSlash);

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
                    .filter(cc -> !cc.isIndependantModule())
                    .map(CalendarCoursEntity::getCoursId)
                    .distinct()
                    .collect(Collectors.toList());
            final List<Integer> idsCoursIndependant = calendarCoursEntities
                    .stream()
                    .filter(CalendarCoursEntity::isIndependantModule)
                    .map(cc -> Integer.parseInt(cc.getCoursId()))
                    .distinct()
                    .collect(Collectors.toList());
            final List<CoursEntity> coursEntities = coursDAOService.findByListIdCours(idsCours);
            final List<IndependantModuleEntity> independantModuleEntities = independantModuleDAOService.findByListId(idsCoursIndependant);
            calendarDetailResponse.setCours(coursEntities);
            calendarDetailResponse.setIndependantModules(independantModuleEntities);
        }

        final List<CalendarConstraintEntity> constraints = calendarConstraintDAOService.findByCalendarId(id);

        if (!constraints.isEmpty()) {
            calendarDetailResponse.setConstraints(constraints);
        }

        return calendarDetailResponse;
    }

    @GetMapping("/{idCalendar}/lines")
    public List<LineCalendarGeneration> getCalendarLinesById(@PathVariable(name = "idCalendar") int id)
            throws RestResponseException {
        final CalendarEntity c = calendarDAOService.findById(id);

        if (c == null) {
            throw new RestResponseException(HttpStatus.NOT_FOUND, "Calendrier non trouvé");
        } else if (c.getState() == CalendarState.DRAFT) {
            throw new RestResponseException(HttpStatus.CONFLICT, "Le calendrier ne doit pas être à l'état de brouillon");
        }

        final List<CalendarCoursEntity> calendarCoursEntities = calendarCoursDAOService.findByCalendarId(id);

        final List<String> idsCours = calendarCoursEntities.stream()
                .filter(cc -> !cc.isIndependantModule())
                .map(CalendarCoursEntity::getCoursId)
                .collect(Collectors.toList());
        final List<Integer> idsCoursIndependant = calendarCoursEntities
                .stream()
                .filter(CalendarCoursEntity::isIndependantModule)
                .map(cc -> Integer.parseInt(cc.getCoursId()))
                .collect(Collectors.toList());
        final List<CoursComplet> coursComplets = coursDAOService.findAllCoursCompletsByIds(idsCours);
        independantModuleDAOService.findByListId(idsCoursIndependant).forEach(im -> {
            CoursComplet coursComplet = new CoursComplet();
            coursComplet.setIdModule(im.getId());
            coursComplet.setLibelleModule(im.getLongName());
            coursComplet.setDureeReelleEnHeures(im.getHours());
            coursComplet.setDebut(im.getStartDate());
            coursComplet.setFin(im.getEndDate());
            coursComplet.setCodeLieu(im.getCodeLieu());
            coursComplets.add(coursComplet);
        });

        return calendarExportUtil.getCalendarLines(c, coursComplets);
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

    @PutMapping("/{idCalendar}/change-state")
    public StringResponse changeStateCalendar(@Valid @RequestBody StateForm form, BindingResult result,
                                              @PathVariable(name = "idCalendar") int id) throws RestResponseException {
        final CalendarEntity c = calendarDAOService.findById(id);

        if (c == null) {
            throw new RestResponseException(HttpStatus.NOT_FOUND, "Calendrier non trouvé");
        } else if (c.getState() == CalendarState.DRAFT) {
            throw new RestResponseException(HttpStatus.CONFLICT, "Le calendrier ne doit pas être à l'état de brouillon");
        }

        if (result.hasErrors()) {
            throw new RestResponseException(HttpStatus.BAD_REQUEST, "Erreur au niveau des champs", result);
        }

        final CalendarState oldState = c.getState();

        c.setState(form.getState());
        calendarDAOService.update(c);

        historyUtil.addLine("Modification du calendrier n°" + id + " - passage de l'état "
                + oldState + " à l'état " + form.getState());

        return new StringResponse("Etat du calendrier mis à jour avec succès");
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

        calendarDAOService.deleteCalendarAndCoursAndConstraints(id);

        historyUtil.addLine("Suppression du calendrier n°" + id);

        return new StringResponse("Calendrier supprimé avec succès");
    }

    @GetMapping("/{idCalendar}/cours-for-generate-calendar")
    public CoursGenerationResponse getCoursForCalendarInGeneration(@PathVariable(name = "idCalendar") int id) throws RestResponseException {
        // Récupération du Calendrier
        final CalendarEntity calendar = calendarDAOService.findById(id);

        if (calendar == null) {
            throw new RestResponseException(HttpStatus.NOT_FOUND, "Calendrier non trouvé");
        } else if (calendar.getState() != CalendarState.DRAFT) {
            throw new RestResponseException(HttpStatus.CONFLICT, "Le calendrier doit être à l'état de brouillon");
        }

        // Récupération des contraintes du calendrier
        final List<CalendarConstraintEntity> constraints = calendarConstraintDAOService.findByCalendarId(id);
        final CoursGenerationResponse coursGenerationResponse = new CoursGenerationResponse();
        List<CoursComplet> cours;

        final List<Integer> independantModuleIds = constraints.stream()
                .filter(c -> c.getConstraintType().equals(ConstraintType.AJOUT_MODULE_INDEPENDANT))
                .map(c -> Integer.parseInt(c.getConstraintValue()))
                .distinct().collect(Collectors.toList());

        List<IndependantModuleEntity> independantModuleEntities = independantModuleDAOService.findByListId(independantModuleIds);

        // Récupérer les codes des lieux
        final List<Integer> lieux = constraints.stream()
                .filter(c -> c.getConstraintType().equals(ConstraintType.LIEUX))
                .map(c -> Integer.parseInt(c.getConstraintValue()))
                .distinct().collect(Collectors.toList());

        // Récupérer les cours par lieux ou bien tout les cours le cas échéant
        cours = lieux.size() == 0 ? coursDAOService.findAllCoursComplets()
                : coursDAOService.findAllCoursCompletsByLieux(lieux);

        // Exclure les cours hors date de début ou date de fin
        if (calendar.getStartDate() != null || calendar.getEndDate() != null) {
            cours = cours
                    .stream().filter(c -> AlterDateUtil.isIncludeInPeriode(calendar.getStartDate(), calendar.getEndDate(), c.getDebut())
                            || AlterDateUtil.isIncludeInPeriode(calendar.getStartDate(), calendar.getEndDate(), c.getFin()))
                    .collect(Collectors.toList());

            independantModuleEntities = independantModuleEntities
                    .stream().filter(c -> AlterDateUtil.isIncludeInPeriode(calendar.getStartDate(), calendar.getEndDate(), c.getStartDate())
                            || AlterDateUtil.isIncludeInPeriode(calendar.getStartDate(), calendar.getEndDate(), c.getEndDate()))
                    .collect(Collectors.toList());
        }

        // Récupérer les périodes d'exclusion
        final List<ExcludeConstraint> contraintesExclusion = constraints
                .stream().filter(c -> c.getConstraintType().equals(ConstraintType.DISPENSE_PERIODE))
                .map(c -> {
                    final String startString = c.getConstraintValue().split(" - ")[0];
                    final String endString = c.getConstraintValue().split(" - ")[1];
                    try {
                        return new ExcludeConstraint(dateFormat.parse(startString), dateFormat.parse(endString));
                    } catch (ParseException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull).collect(Collectors.toList());

        // Si période d'exclusion, retirer les cours étant dans cette période
        if (!contraintesExclusion.isEmpty()) {
            // Vérification qu'un cours ne ce passe pas durant une période d'exclusion
            // Si oui on le retire de la liste
            cours = cours.stream().filter(c -> {
                boolean notInsideExcludePeriode = true;
                for (ExcludeConstraint ce : contraintesExclusion) {
                    if (AlterDateUtil.isIncludeInPeriode(ce.getStart(), ce.getEnd(), c.getDebut())
                            || AlterDateUtil.isIncludeInPeriode(ce.getStart(), ce.getEnd(), c.getFin())) {
                        notInsideExcludePeriode = false;
                    }
                }
                return notInsideExcludePeriode;
            }).collect(Collectors.toList());

            independantModuleEntities = independantModuleEntities.stream().filter(c -> {
                boolean notInsideExcludePeriode = true;
                for (ExcludeConstraint ce : contraintesExclusion) {
                    if (AlterDateUtil.isIncludeInPeriode(ce.getStart(), ce.getEnd(), c.getStartDate())
                            || AlterDateUtil.isIncludeInPeriode(ce.getStart(), ce.getEnd(), c.getEndDate())) {
                        notInsideExcludePeriode = false;
                    }
                }
                return notInsideExcludePeriode;
            }).collect(Collectors.toList());
        }

        // Liste des cours des formations et des modules
        final List<CoursEntity> coursFormationModule = new ArrayList<>();

        // Récupérer les contraintes d'ajout de formation
        final List<String> codesFormation = constraints.stream()
                .filter(c -> c.getConstraintType().equals(ConstraintType.AJOUT_FORMATION))
                .map(CalendarConstraintEntity::getConstraintValue)
                .distinct().collect(Collectors.toList());

        // Si formations, récupérer tout les cours correspondants
        if (!codesFormation.isEmpty()) {
            coursFormationModule.addAll(coursDAOService.findByFormations(codesFormation));
        }

        // Récupérer les contraintes d'ajout de module
        final List<Integer> idsModules = constraints.stream()
                .filter(c -> c.getConstraintType().equals(ConstraintType.AJOUT_MODULE))
                .map(c -> Integer.parseInt(c.getConstraintValue()))
                .distinct().collect(Collectors.toList());

        // Si modules, récupérer tout les cours correspondants
        if (!idsModules.isEmpty()) {
            coursFormationModule.addAll(coursDAOService.findByModules(idsModules));
        }

        if (!coursFormationModule.isEmpty()) {
            cours = cours.stream()
                    .filter(c -> coursFormationModule
                            .stream()
                            .filter(cfm -> cfm.getIdCours().equals(c.getIdCours()))
                            .findAny()
                            .orElse(null) != null)
                    .collect(Collectors.toList());
        }

        // Récupérer les contraintes d'exclusion de module
        final List<Integer> idsModulesExclusion = constraints.stream()
                .filter(c -> c.getConstraintType().equals(ConstraintType.DISPENSE_MODULE))
                .map(c -> Integer.parseInt(c.getConstraintValue()))
                .distinct().collect(Collectors.toList());

        // Si modules, récupérer tout les cours correspondants
        if (!idsModulesExclusion.isEmpty()) {
            final List<CoursEntity> coursToExclude = new ArrayList<>(coursDAOService.findByModules(idsModulesExclusion));

            cours = cours.stream()
                    .filter(c -> coursToExclude
                            .stream()
                            .filter(cte -> cte.getIdCours().equals(c.getIdCours()))
                            .findAny()
                            .orElse(null) == null)
                    .collect(Collectors.toList());
        }

        coursGenerationResponse.setCours(cours);
        coursGenerationResponse.setIndependantModules(independantModuleEntities);

        return coursGenerationResponse;
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

        if (cal.getState() == CalendarState.VALIDATED) {
            throw new RestResponseException(HttpStatus.CONFLICT, "Le calendrier ne doit pas être à l'état de validé");
        }

        if (result.hasErrors()) {
            throw new RestResponseException(HttpStatus.BAD_REQUEST, "Erreur au niveau des champs", result);
        }

        // Enlever les ids en doublon
        final List<String> coursIds = form.getCoursIds().stream().distinct().collect(Collectors.toList());
        final List<Integer> independantModuleIds = form.getCoursIndependantIds().stream().distinct().collect(Collectors.toList());

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

        // Vérifier que les ids correspondent en base
        final StringJoiner errorIndependantIds = new StringJoiner(", ");
        independantModuleIds.forEach(im -> {
            if (!independantModuleDAOService.existsById(im)) {
                errorIndependantIds.add(im.toString());
            }
        });

        if (errorIds.length() != 0) {
            result.addError(new FieldError("coursIndependantIds", "coursIndependantIds",
                    "Les modules indépendants suivant <" + errorIndependantIds.toString() + "> n'existent pas."));
        }

        if (result.hasErrors()) {
            throw new RestResponseException(HttpStatus.BAD_REQUEST, "Erreur au niveau des champs", result);
        }

        final List<CalendarCoursEntity> calendarCours = coursIds.stream().map(c -> {
            final CalendarCoursEntity calendarCoursEntity = new CalendarCoursEntity();
            calendarCoursEntity.setCalendarId(cal.getId());
            calendarCoursEntity.setCoursId(c);
            calendarCoursEntity.setIndependantModule(false);
            return calendarCoursEntity;
        }).collect(Collectors.toList());

        final List<CalendarCoursEntity> calendarCoursIndependant = independantModuleIds.stream().map(im -> {
            final CalendarCoursEntity calendarCoursEntity = new CalendarCoursEntity();
            calendarCoursEntity.setCalendarId(cal.getId());
            calendarCoursEntity.setCoursId(Integer.toString(im));
            calendarCoursEntity.setIndependantModule(true);
            return calendarCoursEntity;
        }).collect(Collectors.toList());

        calendarCoursDAOService.createAll(calendarCours);
        calendarCoursDAOService.createAll(calendarCoursIndependant);
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
