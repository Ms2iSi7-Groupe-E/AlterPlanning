package fr.nantes.eni.alterplanning.controller.api;

import fr.nantes.eni.alterplanning.dao.sqlserver.entity.FormationEntity;
import fr.nantes.eni.alterplanning.exception.RestResponseException;
import fr.nantes.eni.alterplanning.service.dao.FormationDAOService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/formation")
public class FormationController {

    @Resource
    private FormationDAOService formationDAOService;

    @GetMapping("")
    public List<FormationEntity> getFormations() {
        return formationDAOService.findAll();
    }

    @GetMapping("/{codeFormation}")
    public FormationEntity getFormationById(@PathVariable(name = "codeFormation") String codeFormation) throws RestResponseException {
        final FormationEntity f = formationDAOService.findById(codeFormation);

        if (f == null) {
            throw new RestResponseException(HttpStatus.NOT_FOUND, "Formation not found");
        }

        return f;
    }
}