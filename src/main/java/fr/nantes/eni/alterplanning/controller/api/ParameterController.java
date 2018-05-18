package fr.nantes.eni.alterplanning.controller.api;

import fr.nantes.eni.alterplanning.dao.mysql.entity.ParameterEntity;
import fr.nantes.eni.alterplanning.dao.sqlserver.entity.TitreEntity;
import fr.nantes.eni.alterplanning.exception.RestResponseException;
import fr.nantes.eni.alterplanning.service.dao.ParameterDAOService;
import fr.nantes.eni.alterplanning.service.dao.TitreDAOService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/parameter")
public class ParameterController {

    @Resource
    private ParameterDAOService parameterDAOService;

    @GetMapping("")
    public List<ParameterEntity> getParameters() {
        return parameterDAOService.findAll();
    }

    @GetMapping("/{key}")
    public ParameterEntity getParameterByKey(@PathVariable(name = "key") String key) throws RestResponseException {
        final ParameterEntity t = parameterDAOService.findById(key);

        if (t == null) {
            throw new RestResponseException(HttpStatus.NOT_FOUND, "Parameter not found");
        }

        return t;
    }

}
