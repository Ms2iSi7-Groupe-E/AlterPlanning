package fr.nantes.eni.alterplanning.service.dao;

import fr.nantes.eni.alterplanning.dao.mysql.entity.HistoryEntity;
import fr.nantes.eni.alterplanning.dao.mysql.repository.IHistoryRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by ughostephan on 23/06/2017.
 */
@Service
public class HistoryDAOService {

    @Resource
    private IHistoryRepository repository;

    public List<HistoryEntity> findAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public HistoryEntity create(final HistoryEntity historyEntity) {
        return repository.save(historyEntity);
    }
}
