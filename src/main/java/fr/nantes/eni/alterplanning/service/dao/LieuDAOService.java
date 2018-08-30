package fr.nantes.eni.alterplanning.service.dao;

import fr.nantes.eni.alterplanning.dao.sqlserver.entity.LieuEntity;
import fr.nantes.eni.alterplanning.dao.sqlserver.repository.ILieuRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by ughostephan on 23/06/2017.
 */
@Service
public class LieuDAOService {

    @Resource
    private ILieuRepository repository;

    public List<LieuEntity> findAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public LieuEntity findById(final Integer codeLieu) {
        return repository.findById(codeLieu).orElse(null);
    }

    public List<LieuEntity> findAllTeachingCours() {
        return repository.findAllTeachingCours();
    }

    public boolean existById(final Integer codeLieu) {
        return repository.existsById(codeLieu);
    }
}
