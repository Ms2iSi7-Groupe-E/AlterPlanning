package fr.nantes.eni.alterplanning.service.dao;

import fr.nantes.eni.alterplanning.dao.sqlserver.entity.CoursEntity;
import fr.nantes.eni.alterplanning.dao.sqlserver.repository.ICoursRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by ughostephan on 23/06/2017.
 */
@Service
public class CoursDAOService {

    @Resource
    private ICoursRepository repository;

    public List<CoursEntity> findAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public CoursEntity findById(final String id) {
        return repository.findById(id).orElse(null);
    }

    public List<CoursEntity> findByCodePromotion(final String codePromotion) {
        return repository.findAllByCodePromotion(codePromotion);
    }
}
