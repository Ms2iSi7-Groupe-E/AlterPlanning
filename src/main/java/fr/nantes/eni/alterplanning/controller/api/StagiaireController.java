package fr.nantes.eni.alterplanning.controller.api;

import fr.nantes.eni.alterplanning.dao.sqlserver.entity.StagiaireEntity;
import fr.nantes.eni.alterplanning.exception.RestResponseException;
import fr.nantes.eni.alterplanning.service.dao.StagiaireDAOService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/stagiaire")
public class StagiaireController {

    @Resource
    private StagiaireDAOService stagiaireDAOService;

    @GetMapping("")
    public List<StagiaireEntity> getStagiaires() {
        return stagiaireDAOService.findAll();
    }

    @GetMapping("/{id}")
    public StagiaireEntity getStagiaireById(@PathVariable(name = "id") Integer id) throws RestResponseException {
        final StagiaireEntity s = stagiaireDAOService.findById(id);

        if (s == null) {
            throw new RestResponseException(HttpStatus.NOT_FOUND, "Stagiaire not found");
        }

        return s;
    }
}