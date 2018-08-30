package fr.nantes.eni.alterplanning.controller.api;

import fr.nantes.eni.alterplanning.dao.mysql.entity.IndependantModuleEntity;
import fr.nantes.eni.alterplanning.service.dao.IndependantModuleDAOService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/independant-module")
public class IndependantModuleController {

    @Resource
    private IndependantModuleDAOService independantModuleDAOService;

    @GetMapping("")
    public List<IndependantModuleEntity> getCoursIndependantModules() {
        return independantModuleDAOService.findAll();
    }

//    @GetMapping("/{idCours}")
//    public CoursEntity getCourById(@PathVariable(name = "idCours") String id) throws RestResponseException {
//        final CoursEntity c = coursDAOService.findById(id);
//
//        if (c == null) {
//            throw new RestResponseException(HttpStatus.NOT_FOUND, "Cours non trouvé");
//        }
//
//        return c;
//    }
}