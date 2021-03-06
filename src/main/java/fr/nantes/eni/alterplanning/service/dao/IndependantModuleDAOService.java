package fr.nantes.eni.alterplanning.service.dao;

import fr.nantes.eni.alterplanning.dao.mysql.entity.IndependantModuleEntity;
import fr.nantes.eni.alterplanning.dao.mysql.repository.IIndependantModuleRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by ughostephan on 30/08/2018
 */
@Service
public class IndependantModuleDAOService {

    @Resource
    private IIndependantModuleRepository repository;

    public List<IndependantModuleEntity> findAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public IndependantModuleEntity findById(final int id) {
        return repository.findById(id).orElse(null);
    }

    public List<IndependantModuleEntity> findByListId(List<Integer> ids) {
        if (ids != null && ids.size() > 0) {
            return repository.findByListId(ids);
        }
        return new ArrayList<>();
    }

    public IndependantModuleEntity create(final IndependantModuleEntity entity) {
        return repository.save(entity);
    }

    public void update(final IndependantModuleEntity module) {
        IndependantModuleEntity entity = repository.findById(module.getId()).orElse(null);

        if (entity == null)
            throw new EntityNotFoundException();

        repository.save(module);
    }

    public void delete(final int id) {
        repository.deleteById(id);
    }

    public boolean existsById(Integer id) {
        return repository.existsById(id);
    }
}
