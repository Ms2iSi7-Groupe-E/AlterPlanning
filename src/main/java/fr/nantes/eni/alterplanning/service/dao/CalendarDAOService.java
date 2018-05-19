package fr.nantes.eni.alterplanning.service.dao;

import fr.nantes.eni.alterplanning.dao.mysql.entity.CalendarEntity;
import fr.nantes.eni.alterplanning.dao.mysql.entity.UserEntity;
import fr.nantes.eni.alterplanning.dao.mysql.repository.ICalendarRepository;
import fr.nantes.eni.alterplanning.dao.mysql.repository.IUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
public class CalendarDAOService {

    @Resource
    private ICalendarRepository repository;

    public List<CalendarEntity> findAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public CalendarEntity findById(final Integer id) {
        return repository.findById(id).orElse(null);
    }

    public CalendarEntity create(final CalendarEntity calendar) {
        return repository.save(calendar);
    }

    public void update(final CalendarEntity calendar) {
        CalendarEntity entity = repository.findById(calendar.getId()).orElse(null);

        if (entity == null)
            throw new EntityNotFoundException();

        repository.save(calendar);
    }

    public void delete(final Integer id) {
        CalendarEntity entityToDelete = repository.findById(id).orElse(null);

        if (entityToDelete == null)
            throw new EntityNotFoundException();

        repository.delete(entityToDelete);
    }
}
