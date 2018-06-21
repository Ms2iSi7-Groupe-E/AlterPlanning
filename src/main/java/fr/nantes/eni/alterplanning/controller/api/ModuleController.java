package fr.nantes.eni.alterplanning.controller.api;

import fr.nantes.eni.alterplanning.dao.mysql.entity.ModuleRequirementEntity;
import fr.nantes.eni.alterplanning.dao.sqlserver.entity.CoursEntity;
import fr.nantes.eni.alterplanning.dao.sqlserver.entity.ModuleEntity;
import fr.nantes.eni.alterplanning.exception.RestResponseException;
import fr.nantes.eni.alterplanning.model.form.ModuleRequirementForm;
import fr.nantes.eni.alterplanning.model.response.ModuleRequirementResponse;
import fr.nantes.eni.alterplanning.model.response.RequirementResponse;
import fr.nantes.eni.alterplanning.model.response.StringResponse;
import fr.nantes.eni.alterplanning.service.dao.CoursDAOService;
import fr.nantes.eni.alterplanning.service.dao.ModuleDAOService;
import fr.nantes.eni.alterplanning.service.dao.ModuleRequirementDAOService;
import fr.nantes.eni.alterplanning.util.HistoryUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/module")
public class ModuleController {

    @Resource
    private HistoryUtil historyUtil;

    @Resource
    private ModuleDAOService moduleDAOService;

    @Resource
    private ModuleRequirementDAOService moduleRequirementDAOService;

    @Resource
    private CoursDAOService coursDAOService;

    @GetMapping("")
    public List<ModuleEntity> getModules() {
        return moduleDAOService.findAll();
    }

    @GetMapping("/{idModule}")
    public ModuleEntity getModuleById(@PathVariable(name = "idModule") Integer idModule) throws RestResponseException {
        final ModuleEntity m = moduleDAOService.findById(idModule);

        if (m == null) {
            throw new RestResponseException(HttpStatus.NOT_FOUND, "Module non trouvé");
        }

        return m;
    }

    @GetMapping("/{idModule}/cours")
    public List<CoursEntity> getCoursByModule(@PathVariable(name = "idModule") Integer idModule) throws RestResponseException {
        final ModuleEntity m = moduleDAOService.findById(idModule);

        if (m == null) {
            throw new RestResponseException(HttpStatus.NOT_FOUND, "Module non trouvé");
        }

        return coursDAOService.findByModule(idModule);
    }

    @GetMapping("/with-requirement")
    @ApiOperation(value = "", notes = "Permet de retourner tous les modules ayant des pré-requis (avec leurs pré-requis).")
    public List<ModuleRequirementResponse> getModulesWithRequirement() {
        final List<ModuleRequirementEntity> moduleRequirementEntities = moduleRequirementDAOService.findAll();
        final HashMap<Integer, List<RequirementResponse>> hm = new HashMap<>();

        moduleRequirementEntities.forEach(mre -> {
            List<RequirementResponse> requirementResponses = new ArrayList<>();

            if (hm.containsKey(mre.getModuleId())) {
                requirementResponses = hm.get(mre.getModuleId());
            }

            RequirementResponse requirement = new RequirementResponse();
            requirement.setModuleId(mre.getRequiredModuleId());
            requirement.setOr(mre.isOr());
            requirementResponses.add(requirement);

            hm.put(mre.getModuleId(), requirementResponses);
        });

        final List<ModuleRequirementResponse> response = new ArrayList<>();
        hm.forEach((k, v) -> {
            ModuleRequirementResponse moduleRequirementResponse = new ModuleRequirementResponse();
            moduleRequirementResponse.setModuleId(k);
            moduleRequirementResponse.setRequirements(v);
            response.add(moduleRequirementResponse);
        });

        return response;
    }

    @GetMapping("/{idModule}/requirement")
    @ApiOperation(value = "", notes = "Permet de retourner les pré-requis d'un module en particulier")
    public ModuleRequirementResponse getRequirementByModule(@PathVariable(name = "idModule") Integer idModule) throws RestResponseException {
        final ModuleEntity m = moduleDAOService.findById(idModule);

        if (m == null) {
            throw new RestResponseException(HttpStatus.NOT_FOUND, "Module non trouvé");
        }

        final List<ModuleRequirementEntity> moduleRequirementEntities = moduleRequirementDAOService.findByModule(idModule);
        final ModuleRequirementResponse response = new ModuleRequirementResponse();
        response.setModuleId(idModule);

        moduleRequirementEntities.forEach(mre -> {
            RequirementResponse requirement = new RequirementResponse();
            requirement.setModuleId(mre.getRequiredModuleId());
            requirement.setOr(mre.isOr());
            response.addRequirement(requirement);
        });

        return response;
    }

    @PostMapping("/{idModule}/requirement")
    @ApiOperation(value = "", notes = "Service permettant d'ajouter un pré-requis à un module")
    public StringResponse addRequirementForModule(@Valid @RequestBody ModuleRequirementForm form,
                                                  BindingResult result,
                                                  @PathVariable(name = "idModule") int idModule) throws RestResponseException {
        final ModuleEntity m = moduleDAOService.findById(idModule);

        if (m == null) {
            throw new RestResponseException(HttpStatus.NOT_FOUND, "Module non trouvé");
        }

        ModuleEntity requiredModule = null;

        if (form.getRequiredModuleId() != null) {
            requiredModule = moduleDAOService.findById(form.getRequiredModuleId());

            if (requiredModule == null) {
                result.addError(new FieldError("requiredModuleId",  "requiredModuleId", "innexistant en base"));
            }

            if (requiredModule != null && form.getRequiredModuleId().equals(idModule)) {
                result.addError(new FieldError("requiredModuleId",  "requiredModuleId", "ne peut pas être identique au module"));
            }
        }

        // If no error yet, check if constraint 'module_requirements_uq' is OK
        if (!result.hasErrors() && moduleRequirementDAOService.alreadyExist(idModule, form.getRequiredModuleId(), form.getOr())) {
            throw new RestResponseException(HttpStatus.CONFLICT, "Le prérequis existe déjà en base");
        }

        if (result.hasErrors()) {
            throw new RestResponseException(HttpStatus.BAD_REQUEST, "Erreur au niveau des champs", result);
        }

        ModuleRequirementEntity entity = new ModuleRequirementEntity();
        entity.setModuleId(idModule);
        entity.setOr(form.getOr());
        entity.setRequiredModuleId(form.getRequiredModuleId());

        moduleRequirementDAOService.create(entity);

        historyUtil.addLine("Ajout du prérequis \"" 
                + requiredModule.getLibelle()
                + "\" au module \"" 
                + m.getLibelle() + "\"");

        return new StringResponse("Prérequis ajouté avec succès pour le module " + idModule);
    }

    @DeleteMapping("/{idModule}/requirement")
    public StringResponse deleteRequirementByModule(@Valid @RequestBody ModuleRequirementForm form,
                                                    BindingResult result,
                                                    @PathVariable(name = "idModule") Integer idModule) throws RestResponseException {
        if (result.hasErrors()) {
            throw new RestResponseException(HttpStatus.BAD_REQUEST, "Erreur au niveau des champs", result);
        }

        ModuleRequirementEntity entity = new ModuleRequirementEntity();
        entity.setModuleId(idModule);
        entity.setRequiredModuleId(form.getRequiredModuleId());
        entity.setOr(form.getOr());

        final Integer moduleRequirementId = moduleRequirementDAOService.findIdByUniqueConstraint(entity);

        if (moduleRequirementId == null) {
            throw new RestResponseException(HttpStatus.NOT_FOUND, "Prérequis non trouvée");
        }

        moduleRequirementDAOService.delete(moduleRequirementId);

        final ModuleEntity module = moduleDAOService.findById(idModule);
        final ModuleEntity requiredModule = moduleDAOService.findById(form.getRequiredModuleId());

        historyUtil.addLine("Suppression du prérequis \""
                + requiredModule.getLibelle()
                + "\" pour le module \""
                + module.getLibelle() + "\"");

        return new StringResponse("Le prérequis à été supprimé avec succès");
    }
}