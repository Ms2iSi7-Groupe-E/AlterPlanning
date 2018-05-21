package fr.nantes.eni.alterplanning.service.dao;

import fr.nantes.eni.alterplanning.dao.sqlserver.entity.EntrepriseEntity;
import fr.nantes.eni.alterplanning.dao.sqlserver.repository.IEntrepriseRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by ughostephan on 23/06/2017.
 */
@Service
public class EntrepriseDAOService {

    @Resource
    private IEntrepriseRepository repository;

    public List<EntrepriseEntity> findAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public EntrepriseEntity findById(final Integer id) {
        return repository.findById(id).orElse(null);
    }

    public EntrepriseEntity findByStagiaire(final Integer codeStagiaire) {
        return repository.findByStagiaire(codeStagiaire).orElse(null);
    }

    public boolean existsById(final Integer id) {
        return repository.existsById(id);
    }
}
