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
@RequestMapping("/api/entreprise")
public class EntrepriseController {

    @Resource
    private EntrepriseDAOService entrepriseDAOService;

    @Resource
    private StagiaireDAOService stagiaireDAOService;

    @GetMapping("")
    public List<EntrepriseEntity> getEntreprises() {
        return entrepriseDAOService.findAll();
    }

    @GetMapping("/{codeEntreprise}")
    public EntrepriseEntity getEntrepriseById(@PathVariable(name = "codeEntreprise") Integer codeEntreprise)
            throws RestResponseException {
        final EntrepriseEntity s = entrepriseDAOService.findById(codeEntreprise);

        if (s == null) {
            throw new RestResponseException(HttpStatus.NOT_FOUND, "Entreprise non trouvée");
        }

        return s;
    }

    @GetMapping("/{codeEntreprise}/stagiaires")
    public List<StagiaireEntity> getStagiairesForEntreprise(@PathVariable(name = "codeEntreprise") Integer codeEntreprise)
            throws RestResponseException {
        final EntrepriseEntity s = entrepriseDAOService.findById(codeEntreprise);

        if (s == null) {
            throw new RestResponseException(HttpStatus.NOT_FOUND, "Entreprise non trouvée");
        }

        return stagiaireDAOService.findAllByEntreprise(codeEntreprise);
    }
}