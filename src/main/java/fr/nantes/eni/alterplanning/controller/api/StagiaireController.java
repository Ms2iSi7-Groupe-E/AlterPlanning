package fr.nantes.eni.alterplanning.controller.api;

import fr.nantes.eni.alterplanning.dao.sqlserver.entity.EntrepriseEntity;
import fr.nantes.eni.alterplanning.dao.sqlserver.entity.StagiaireEntity;
import fr.nantes.eni.alterplanning.exception.RestResponseException;
import fr.nantes.eni.alterplanning.service.dao.EntrepriseDAOService;
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

    @Resource
    private EntrepriseDAOService entrepriseDAOService;

    @GetMapping("")
    public List<StagiaireEntity> getStagiaires() {
        return stagiaireDAOService.findAll();
    }

    @GetMapping("/{codeStagiaire}")
    public StagiaireEntity getStagiaireById(@PathVariable(name = "codeStagiaire") Integer codeStagiaire)
            throws RestResponseException {
        final StagiaireEntity s = stagiaireDAOService.findById(codeStagiaire);

        if (s == null) {
            throw new RestResponseException(HttpStatus.NOT_FOUND, "Stagiaire non trouvé");
        }

        return s;
    }

    @GetMapping("/{codeStagiaire}/entreprises")
    public List<EntrepriseEntity> getEntreprisesForStagiaire(@PathVariable(name = "codeStagiaire") Integer codeStagiaire)
            throws RestResponseException {
        final StagiaireEntity s = stagiaireDAOService.findById(codeStagiaire);

        if (s == null) {
            throw new RestResponseException(HttpStatus.NOT_FOUND, "Stagiaire non trouvé");
        }

        return entrepriseDAOService.findByStagiaire(codeStagiaire);
    }
}