package fr.nantes.eni.alterplanning.service.dao;

import fr.nantes.eni.alterplanning.dao.sqlserver.entity.PromotionEntity;
import fr.nantes.eni.alterplanning.dao.sqlserver.repository.IPromotionRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by ughostephan on 23/06/2017.
 */
@Service
public class PromotionDAOService {

    @Resource
    private IPromotionRepository repository;

    public List<PromotionEntity> findAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public PromotionEntity findById(final String id) {
        return repository.findById(id).orElse(null);
    }
}
