package fr.nantes.eni.alterplanning.controller.api;

import fr.nantes.eni.alterplanning.dao.sqlserver.entity.CoursEntity;
import fr.nantes.eni.alterplanning.dao.sqlserver.entity.LieuEntity;
import fr.nantes.eni.alterplanning.exception.RestResponseException;
import fr.nantes.eni.alterplanning.service.dao.CoursDAOService;
import fr.nantes.eni.alterplanning.service.dao.LieuDAOService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/lieu")
public class LieuController {

    @Resource
    private LieuDAOService lieuDAOService;

    @Resource
    private CoursDAOService coursDAOService;

    @GetMapping("")
    public List<LieuEntity> getLieux(@RequestParam(value = "with-courses", required = false, defaultValue = "false") final Boolean withCourses) {
        if (withCourses) {
            return lieuDAOService.findAllTeachingCours();
        } else {
            return lieuDAOService.findAll();
        }
    }

    @GetMapping("/{codeLieu}")
    public LieuEntity getLieuById(@PathVariable(name = "codeLieu") Integer codeLieu) throws RestResponseException {
        final LieuEntity l = lieuDAOService.findById(codeLieu);

        if (l == null) {
            throw new RestResponseException(HttpStatus.NOT_FOUND, "Lieu non trouvé");
        }

        return l;
    }

    @GetMapping("/{codeLieu}/cours")
    public List<CoursEntity> getCoursByLieu(@PathVariable(name = "codeLieu") Integer codeLieu) throws RestResponseException {
        final LieuEntity l = lieuDAOService.findById(codeLieu);

        if (l == null) {
            throw new RestResponseException(HttpStatus.NOT_FOUND, "Lieu non trouvé");
        }

        return coursDAOService.findByLieu(codeLieu);
    }
}