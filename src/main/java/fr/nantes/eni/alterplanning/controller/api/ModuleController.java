package fr.nantes.eni.alterplanning.controller.api;

import fr.nantes.eni.alterplanning.dao.mysql.entity.ModuleRequirementEntity;
import fr.nantes.eni.alterplanning.dao.sqlserver.entity.CoursEntity;
import fr.nantes.eni.alterplanning.dao.sqlserver.entity.ModuleEntity;
import fr.nantes.eni.alterplanning.exception.RestResponseException;
import fr.nantes.eni.alterplanning.model.form.AddCalendarForm;
import fr.nantes.eni.alterplanning.model.form.AddModuleRequirementForm;
import fr.nantes.eni.alterplanning.model.response.StringResponse;
import fr.nantes.eni.alterplanning.service.dao.CoursDAOService;
import fr.nantes.eni.alterplanning.service.dao.ModuleDAOService;
import fr.nantes.eni.alterplanning.service.dao.ModuleRequirementDAOService;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/module")
public class ModuleController {

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
            throw new RestResponseException(HttpStatus.NOT_FOUND, "Module not found");
        }

        return m;
    }

    @GetMapping("/{idModule}/cours")
    public List<CoursEntity> getCoursByModule(@PathVariable(name = "idModule") Integer idModule) throws RestResponseException {
        final ModuleEntity m = moduleDAOService.findById(idModule);

        if (m == null) {
            throw new RestResponseException(HttpStatus.NOT_FOUND, "Module not found");
        }

        return coursDAOService.findByModule(idModule);
    }

    @GetMapping("/{idModule}/requirement")
    public List<ModuleRequirementEntity> getRequirementByModule(@PathVariable(name = "idModule") Integer idModule) throws RestResponseException {
        final ModuleEntity m = moduleDAOService.findById(idModule);

        if (m == null) {
            throw new RestResponseException(HttpStatus.NOT_FOUND, "Module not found");
        }

        return moduleRequirementDAOService.findByModule(idModule);
    }

    @PostMapping("/{idModule}/requirement")
    public ModuleRequirementEntity addRequirementForModule(@Valid @RequestBody AddModuleRequirementForm form,
                                                           BindingResult result,
                                                           @PathVariable(name = "idModule") int idModule) throws RestResponseException {
        final ModuleEntity m = moduleDAOService.findById(idModule);

        if (m == null) {
            throw new RestResponseException(HttpStatus.NOT_FOUND, "Module not found");
        }

        if (form.getRequiredModuleId() != null) {
            final ModuleEntity requiredModule = moduleDAOService.findById(form.getRequiredModuleId());

            if (requiredModule == null) {
                result.addError(new FieldError("requiredModuleId",  "requiredModuleId", "not exist"));
            }

            if (requiredModule != null && form.getRequiredModuleId().equals(idModule)) {
                result.addError(new FieldError("requiredModuleId",  "requiredModuleId", "cannot be the same as idModule"));
            }
        }

        // TODO : check if already exist : constraint 'module_requirements_uq'

        if (result.hasErrors()) {
            throw new RestResponseException(HttpStatus.BAD_REQUEST, "Bad request", result);
        }

        ModuleRequirementEntity entity = new ModuleRequirementEntity();
        entity.setModuleId(idModule);
        entity.setOr(form.getOr());
        entity.setRequiredModuleId(form.getRequiredModuleId());

        return moduleRequirementDAOService.create(entity);
    }

    @PutMapping("/{idModule}/requirement")
    public StringResponse updateRequirementByModule(@PathVariable(name = "idModule") Integer idModule) throws RestResponseException {
        // TODO
        throw new RestResponseException(HttpStatus.NOT_IMPLEMENTED, "Not yet implemented");
    }

    @DeleteMapping("/{idModule}/requirement")
    public StringResponse deleteRequirementByModule(@PathVariable(name = "idModule") Integer idModule) throws RestResponseException {
        // TODO
        throw new RestResponseException(HttpStatus.NOT_IMPLEMENTED, "Not yet implemented");
    }
}