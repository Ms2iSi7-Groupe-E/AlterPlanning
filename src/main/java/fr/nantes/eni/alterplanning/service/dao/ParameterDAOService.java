package fr.nantes.eni.alterplanning.service.dao;

import fr.nantes.eni.alterplanning.dao.mysql.entity.ParameterEntity;
import fr.nantes.eni.alterplanning.dao.mysql.repository.IParameterRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ParameterDAOService {

    @Resource
    private IParameterRepository repository;

    public List<ParameterEntity> findAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public ParameterEntity findById(final String key) {
        return repository.findById(key).orElse(null);
    }

    public ParameterEntity create(final ParameterEntity parameter){
        return repository.save(parameter);
    }

    public void update(ParameterEntity parameter){
        ParameterEntity paramInDb = repository.findById(parameter.getKey()).orElse(null);

        if(paramInDb == null)
            throw new EntityNotFoundException();

        repository.save(parameter);
    }
}
