package fr.nantes.eni.alterplanning.service.dao;

import fr.nantes.eni.alterplanning.dao.mysql.entity.ModuleRequirementEntity;
import fr.nantes.eni.alterplanning.dao.mysql.repository.IModuleRequirementRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by ughostephan on 23/06/2017.
 */
@Service
public class ModuleRequirementDAOService {

    @Resource
    private IModuleRequirementRepository repository;

    public List<ModuleRequirementEntity> findAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public ModuleRequirementEntity findById(final Integer id) {
        return repository.findById(id).orElse(null);
    }

    public List<ModuleRequirementEntity> findByModule(final Integer idModule) {
        return repository.findByModule(idModule);
    }

    public ModuleRequirementEntity create(final ModuleRequirementEntity moduleRequirement) {
        return repository.save(moduleRequirement);
    }

    public void update(final ModuleRequirementEntity moduleRequirement) {
        ModuleRequirementEntity entity = repository.findById(moduleRequirement.getId()).orElse(null);

        if (entity == null)
            throw new EntityNotFoundException();

        repository.save(moduleRequirement);
    }

    public void delete(final Integer id) {
        repository.deleteById(id);
    }
}
