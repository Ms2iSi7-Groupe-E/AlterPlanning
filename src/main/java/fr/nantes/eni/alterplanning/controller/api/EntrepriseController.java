package fr.nantes.eni.alterplanning.controller.api;

import fr.nantes.eni.alterplanning.dao.sqlserver.entity.EntrepriseEntity;
import fr.nantes.eni.alterplanning.exception.RestResponseException;
import fr.nantes.eni.alterplanning.service.dao.EntrepriseDAOService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/entreprise")
public class EntrepriseController {

    @Resource
    private EntrepriseDAOService entrepriseDAOService;

    @GetMapping("")
    public List<EntrepriseEntity> getEntreprises() {
        return entrepriseDAOService.findAll();
    }

    @GetMapping("/{id}")
    public EntrepriseEntity getEntrepriseById(@PathVariable(name = "id") Integer id) throws RestResponseException {
        final EntrepriseEntity s = entrepriseDAOService.findById(id);

        if (s == null) {
            throw new RestResponseException(HttpStatus.NOT_FOUND, "Entreprise not found");
        }

        return s;
    }
}