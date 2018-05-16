package fr.nantes.eni.alterplanning.controller.api;

import fr.nantes.eni.alterplanning.dao.sqlserver.entity.ModuleEntity;
import fr.nantes.eni.alterplanning.exception.RestResponseException;
import fr.nantes.eni.alterplanning.service.dao.ModuleDAOService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/module")
public class ModuleController {

    @Resource
    private ModuleDAOService moduleDAOService;

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
}