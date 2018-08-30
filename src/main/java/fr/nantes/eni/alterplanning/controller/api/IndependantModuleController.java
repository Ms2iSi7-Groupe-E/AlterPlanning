package fr.nantes.eni.alterplanning.controller.api;

import fr.nantes.eni.alterplanning.dao.mysql.entity.IndependantModuleEntity;
import fr.nantes.eni.alterplanning.exception.RestResponseException;
import fr.nantes.eni.alterplanning.model.form.IndependantModuleForm;
import fr.nantes.eni.alterplanning.model.response.StringResponse;
import fr.nantes.eni.alterplanning.service.dao.IndependantModuleDAOService;
import fr.nantes.eni.alterplanning.service.dao.LieuDAOService;
import fr.nantes.eni.alterplanning.util.HistoryUtil;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/independant-module")
public class IndependantModuleController {

    @Resource
    private IndependantModuleDAOService independantModuleDAOService;

    @Resource
    private LieuDAOService lieuDAOService;

    @Resource
    private HistoryUtil historyUtil;

    @GetMapping("")
    public List<IndependantModuleEntity> getCoursIndependantModules() {
        return independantModuleDAOService.findAll();
    }

    @GetMapping("/{idIndependantModule}")
    public IndependantModuleEntity getCourIndependantModuleById(@PathVariable(name = "idIndependantModule") int id)
            throws RestResponseException {
        final IndependantModuleEntity im = independantModuleDAOService.findById(id);

        if (im == null) {
            throw new RestResponseException(HttpStatus.NOT_FOUND, "Cours du module indépendant non trouvé");
        }

        return im;
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public IndependantModuleEntity addCourIndependantModule(@Valid @RequestBody IndependantModuleForm form, BindingResult result)
            throws RestResponseException {

        if (form.getEndDate() != null && form.getStartDate() != null && form.getEndDate().before(form.getStartDate())) {
            result.addError(new FieldError("startDate",  "startDate", "doit être avant la date de fin"));
            result.addError(new FieldError("endDate",  "endDate", "doit être après la date de début"));
        }

        if (form.getCodeLieu() != null && !lieuDAOService.existById(form.getCodeLieu())) {
            result.addError(new FieldError("codeLieu",  "codeLieu", "n'existe pas en base"));
        }

        if (result.hasErrors()) {
            throw new RestResponseException(HttpStatus.BAD_REQUEST, "Erreur au niveau des champs", result);
        }

        // Define entity
        final IndependantModuleEntity independantModuleEntity = new IndependantModuleEntity();
        independantModuleEntity.setShortName(form.getShortName());
        independantModuleEntity.setLongName(form.getLongName());
        independantModuleEntity.setStartDate(form.getStartDate());
        independantModuleEntity.setEndDate(form.getEndDate());
        independantModuleEntity.setCodeLieu(form.getCodeLieu());
        independantModuleEntity.setHours(form.getVolumeHoraire());

        // Create Independant Module
        final IndependantModuleEntity entityAdded = independantModuleDAOService.create(independantModuleEntity);

        historyUtil.addLine("Création d'un cours du module indépendant n°" + entityAdded.getId() +
                " (" + entityAdded.getShortName() + " - " + entityAdded.getLongName() + ")");

        return entityAdded;
    }

    @PutMapping("/{idIndependantModule}")
    public StringResponse updateCourIndependantModule(@Valid @RequestBody IndependantModuleForm form, BindingResult result,
                                                      @PathVariable(name = "idIndependantModule") int id) throws RestResponseException {

        // Find IndependantModuleEntity to update
        final IndependantModuleEntity independantModuleToUpdate = independantModuleDAOService.findById(id);

        if (independantModuleToUpdate == null) {
            throw new RestResponseException(HttpStatus.NOT_FOUND, "Cours du module indépendant non trouvé");
        }

        if (form.getEndDate() != null && form.getStartDate() != null && form.getEndDate().before(form.getStartDate())) {
            result.addError(new FieldError("startDate",  "startDate", "doit être avant la date de fin"));
            result.addError(new FieldError("endDate",  "endDate", "doit être après la date de début"));
        }

        if (form.getCodeLieu() != null && !lieuDAOService.existById(form.getCodeLieu())) {
            result.addError(new FieldError("codeLieu",  "codeLieu", "n'existe pas en base"));
        }

        if (result.hasErrors()) {
            throw new RestResponseException(HttpStatus.BAD_REQUEST, "Erreur au niveau des champs", result);
        }

        // Set properties to update
        independantModuleToUpdate.setShortName(form.getShortName());
        independantModuleToUpdate.setLongName(form.getLongName());
        independantModuleToUpdate.setStartDate(form.getStartDate());
        independantModuleToUpdate.setEndDate(form.getEndDate());
        independantModuleToUpdate.setCodeLieu(form.getCodeLieu());
        independantModuleToUpdate.setHours(form.getVolumeHoraire());

        // Update user
        independantModuleDAOService.update(independantModuleToUpdate);

        historyUtil.addLine("Modification du cours du module indépendant n°" + independantModuleToUpdate.getId());

        return new StringResponse("Cours modifié avec succès");
    }

    @DeleteMapping("/{idIndependantModule}")
    public StringResponse deleteCourIndependantModule(@PathVariable(name = "idIndependantModule") int id) throws RestResponseException {
        // Find IndependantModule to delete
        final IndependantModuleEntity im = independantModuleDAOService.findById(id);

        if (im == null) {
            throw new RestResponseException(HttpStatus.NOT_FOUND, "Cours du module indépendant non trouvé");
        }

        independantModuleDAOService.delete(id);

        historyUtil.addLine("Suppression du cours du module indépendant n°" + id);

        return new StringResponse("Cours supprimé avec succès");
    }
}