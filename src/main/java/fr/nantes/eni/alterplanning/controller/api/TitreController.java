package fr.nantes.eni.alterplanning.controller.api;

import fr.nantes.eni.alterplanning.dao.sqlserver.entity.FormationEntity;
import fr.nantes.eni.alterplanning.dao.sqlserver.entity.TitreEntity;
import fr.nantes.eni.alterplanning.exception.RestResponseException;
import fr.nantes.eni.alterplanning.service.dao.FormationDAOService;
import fr.nantes.eni.alterplanning.service.dao.TitreDAOService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/titre")
public class TitreController {

    @Resource
    private TitreDAOService titreDAOService;

    @Resource
    private FormationDAOService formationDAOService;

    @GetMapping("")
    public List<TitreEntity> getTitres() {
        return titreDAOService.findAll();
    }

    @GetMapping("/{codeTitre}")
    public TitreEntity getTitreById(@PathVariable(name = "codeTitre") String codeTitre) throws RestResponseException {
        final TitreEntity t = titreDAOService.findById(codeTitre);

        if (t == null) {
            throw new RestResponseException(HttpStatus.NOT_FOUND, "Titre not found");
        }

        return t;
    }

    @GetMapping("/{codeTitre}/formations")
    public List<FormationEntity> getFormationsByTitre(@PathVariable(name = "codeTitre") String codeTitre) {
        return formationDAOService.findByTitre(codeTitre);
    }
}