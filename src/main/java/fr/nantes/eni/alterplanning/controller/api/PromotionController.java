package fr.nantes.eni.alterplanning.controller.api;

import fr.nantes.eni.alterplanning.dao.sqlserver.entity.CoursEntity;
import fr.nantes.eni.alterplanning.dao.sqlserver.entity.PromotionEntity;
import fr.nantes.eni.alterplanning.exception.RestResponseException;
import fr.nantes.eni.alterplanning.service.dao.CoursDAOService;
import fr.nantes.eni.alterplanning.service.dao.PromotionDAOService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/promotion")
public class PromotionController {

    @Resource
    private PromotionDAOService promotionDAOService;

    @Resource
    private CoursDAOService coursDAOService;

    @GetMapping("")
    public List<PromotionEntity> getPromotions() {
        return promotionDAOService.findAll();
    }

    @GetMapping("/{codePromotion}")
    public PromotionEntity getPromotionById(@PathVariable(name = "codePromotion") String codePromotion) throws RestResponseException {
        final PromotionEntity p = promotionDAOService.findById(codePromotion);

        if (p == null) {
            throw new RestResponseException(HttpStatus.NOT_FOUND, "Promotion not found");
        }

        return p;
    }

    @GetMapping("/{codePromotion}/cours")
    public List<CoursEntity> getCoursByPromotion(@PathVariable(name = "codePromotion") String codePromotion) {
        return coursDAOService.findByPromotion(codePromotion);
    }
}