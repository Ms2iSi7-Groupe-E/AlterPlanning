package fr.nantes.eni.alterplanning.service.dao;

import fr.nantes.eni.alterplanning.dao.sqlserver.entity.TitreEntity;
import fr.nantes.eni.alterplanning.dao.sqlserver.repository.ITitreRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by ughostephan on 23/06/2017.
 */
@Service
public class TitreDAOService {

    @Resource
    private ITitreRepository repository;

    public List<TitreEntity> findAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public TitreEntity findById(final String id) {
        return repository.findById(id).orElse(null);
    }

    public TitreEntity findByFormation(String codeFormation) {
        return repository.findByFormation(codeFormation);
    }
}
