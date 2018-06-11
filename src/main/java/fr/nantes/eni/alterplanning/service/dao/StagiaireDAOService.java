package fr.nantes.eni.alterplanning.service.dao;

import fr.nantes.eni.alterplanning.dao.sqlserver.entity.StagiaireEntity;
import fr.nantes.eni.alterplanning.dao.sqlserver.repository.IStagiaireRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by ughostephan on 23/06/2017.
 */
@Service
public class StagiaireDAOService {

    @Resource
    private IStagiaireRepository repository;

    public List<StagiaireEntity> findAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public StagiaireEntity findById(final Integer id) {
        return repository.findById(id).orElse(null);
    }

    public boolean existsById(final Integer id) {
        return repository.existsById(id);
    }

    public List<StagiaireEntity> findAllByEntreprise(final Integer codeEntreprise) {
        return repository.findByEntreprise(codeEntreprise);
    }
}
