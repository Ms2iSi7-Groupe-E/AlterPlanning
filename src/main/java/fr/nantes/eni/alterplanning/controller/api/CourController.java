package fr.nantes.eni.alterplanning.controller.api;

import fr.nantes.eni.alterplanning.dao.sqlserver.entity.CoursEntity;
import fr.nantes.eni.alterplanning.exception.RestResponseException;
import fr.nantes.eni.alterplanning.service.dao.CoursDAOService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/cours")
public class CourController {

    @Resource
    private CoursDAOService coursDAOService;

    @GetMapping("")
    public List<CoursEntity> getCours() {
        return coursDAOService.findAll();
    }

    @GetMapping("/{id}")
    public CoursEntity getCourById(@PathVariable(name = "id") String id) throws RestResponseException {
        final CoursEntity c = coursDAOService.findById(id);

        if (c == null) {
            throw new RestResponseException(HttpStatus.NOT_FOUND, "Cours not found");
        }

        return c;
    }
}