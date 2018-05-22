package fr.nantes.eni.alterplanning.service.dao;

import fr.nantes.eni.alterplanning.dao.sqlserver.entity.FormationEntity;
import fr.nantes.eni.alterplanning.dao.sqlserver.repository.IFormationRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by ughostephan on 23/06/2017.
 */
@Service
public class FormationDAOService {

    @Resource
    private IFormationRepository repository;

    public List<FormationEntity> findAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public FormationEntity findById(final String codeFormation) {
        return repository.findById(codeFormation).orElse(null);
    }

    public List<FormationEntity> findByTitre(final String codeTitre) {
        return repository.findByTitre(codeTitre);
    }
}
