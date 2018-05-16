package fr.nantes.eni.alterplanning.service.dao;

import fr.nantes.eni.alterplanning.dao.sqlserver.entity.ModuleEntity;
import fr.nantes.eni.alterplanning.dao.sqlserver.repository.IModuleRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by ughostephan on 23/06/2017.
 */
@Service
public class ModuleDAOService {

    @Resource
    private IModuleRepository repository;

    public List<ModuleEntity> findAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public ModuleEntity findById(final Integer id) {
        return repository.findById(id).orElse(null);
    }
}
