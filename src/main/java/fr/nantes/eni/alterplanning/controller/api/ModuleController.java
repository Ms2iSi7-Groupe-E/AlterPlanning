package fr.nantes.eni.alterplanning.controller.api;

import fr.nantes.eni.alterplanning.dao.sqlserver.entity.CoursEntity;
import fr.nantes.eni.alterplanning.dao.sqlserver.entity.ModuleEntity;
import fr.nantes.eni.alterplanning.exception.RestResponseException;
import fr.nantes.eni.alterplanning.service.dao.CoursDAOService;
import fr.nantes.eni.alterplanning.service.dao.ModuleDAOService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/module")
public class ModuleController {

    @Resource
    private ModuleDAOService moduleDAOService;

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
    public List<CoursEntity> getCoursByModule(@PathVariable(name = "idModule") Integer idModule) {
        return coursDAOService.findByModule(idModule);
    }

    @GetMapping("/{idModule}/requirement")
    public List<ModuleEntity> getRequirementByModule(@PathVariable(name = "idModule") Integer idModule) throws RestResponseException {
        // TODO
        throw new RestResponseException(HttpStatus.NOT_IMPLEMENTED, "Not yet implemented");
    }

    @PostMapping("/{idModule}/requirement")
    public List<ModuleEntity> addRequirementForModule(@PathVariable(name = "idModule") Integer idModule) throws RestResponseException {
        // TODO
        throw new RestResponseException(HttpStatus.NOT_IMPLEMENTED, "Not yet implemented");
    }

    @PutMapping("/{idModule}/requirement")
    public List<ModuleEntity> updateRequirementByModule(@PathVariable(name = "idModule") Integer idModule) throws RestResponseException {
        // TODO
        throw new RestResponseException(HttpStatus.NOT_IMPLEMENTED, "Not yet implemented");
    }

    @DeleteMapping("/{idModule}/requirement")
    public List<ModuleEntity> deleteRequirementByModule(@PathVariable(name = "idModule") Integer idModule) throws RestResponseException {
        // TODO
        throw new RestResponseException(HttpStatus.NOT_IMPLEMENTED, "Not yet implemented");
    }
}