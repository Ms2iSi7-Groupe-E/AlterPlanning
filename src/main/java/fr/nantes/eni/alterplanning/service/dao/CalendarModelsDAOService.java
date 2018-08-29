package fr.nantes.eni.alterplanning.service.dao;

import fr.nantes.eni.alterplanning.dao.mysql.entity.CalendarModelEntity;
import fr.nantes.eni.alterplanning.dao.mysql.repository.ICalendarModelsRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by ughostephan on 23/06/2017.
 */
@Service
public class CalendarModelsDAOService {

    @Resource
    private ICalendarModelsRepository repository;

    public List<CalendarModelEntity> findAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public CalendarModelEntity create(final CalendarModelEntity entity) {
        return repository.save(entity);
    }

    public void delete(final int id) {
        repository.deleteById(id);
    }
}
