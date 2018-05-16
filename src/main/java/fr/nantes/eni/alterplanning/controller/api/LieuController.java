package fr.nantes.eni.alterplanning.controller.api;

import fr.nantes.eni.alterplanning.dao.sqlserver.entity.LieuEntity;
import fr.nantes.eni.alterplanning.exception.RestResponseException;
import fr.nantes.eni.alterplanning.service.dao.LieuDAOService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/lieu")
public class LieuController {

    @Resource
    private LieuDAOService lieuDAOService;

    @GetMapping("")
    public List<LieuEntity> getLieux() {
        return lieuDAOService.findAll();
    }

    @GetMapping("/{codeLieu}")
    public LieuEntity getLieuById(@PathVariable(name = "codeLieu") Integer codeLieu) throws RestResponseException {
        final LieuEntity l = lieuDAOService.findById(codeLieu);

        if (l == null) {
            throw new RestResponseException(HttpStatus.NOT_FOUND, "Lieu not found");
        }

        return l;
    }
}