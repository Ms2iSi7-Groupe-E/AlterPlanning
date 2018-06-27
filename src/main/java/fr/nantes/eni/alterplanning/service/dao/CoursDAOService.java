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

    public List<CoursEntity> findByPromotion(final String codePromotion) {
        return repository.findAllByPromotion(codePromotion);
    }

    public boolean existsById(final String id) {
        return repository.existsById(id);
    }

    public List<CoursEntity> findByLieu(final Integer codeLieu) {
        return repository.findAllByLieu(codeLieu);
    }

    public List<CoursEntity> findByModule(final Integer idModule) {
        return repository.findAllByModule(idModule);
    }

    public List<CoursEntity> findByListIdCours(final List<String> idsCours) {
        return repository.findAllByFromListIdCours(idsCours);
    }
}
