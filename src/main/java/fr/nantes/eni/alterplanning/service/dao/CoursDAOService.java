package fr.nantes.eni.alterplanning.service.dao;

import fr.nantes.eni.alterplanning.dao.sqlserver.entity.CoursEntity;
import fr.nantes.eni.alterplanning.dao.sqlserver.repository.ICoursRepository;
import fr.nantes.eni.alterplanning.model.simplebean.CoursComplet;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by ughostephan on 23/06/2017.
 */
@Service
public class CoursDAOService {

    @Resource
    private ICoursRepository repository;

    public List<CoursEntity> findAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public List<CoursComplet> findAllCoursComplets() {
        final List<Object[]> listObj = repository.findAllCoursComplets();
        return convertRawListToCoursComplet(listObj);
    }

    public CoursEntity findById(final String id) {
        return repository.findById(id).orElse(null);
    }

    public List<CoursEntity> findByPromotion(final String codePromotion) {
        return repository.findAllByPromotion(codePromotion);
    }

    public boolean existsById(final String id) {
        return repository.existsById(id);
    }

    public List<CoursEntity> findByLieu(final Integer codeLieu) {
        return repository.findAllByLieu(codeLieu);
    }

    public List<CoursEntity> findByLieux(final List<Integer> codesLieu) {
        return repository.findAllByLieux(codesLieu);
    }

    public List<CoursComplet> findAllCoursCompletsByLieux(final List<Integer> codesLieu) {
        final List<Object[]> listObj = repository.findAllCoursCompletsByLieux(codesLieu);
        return convertRawListToCoursComplet(listObj);
    }

    public List<CoursEntity> findByModule(final Integer idModule) {
        return repository.findAllByModule(idModule);
    }

    public List<CoursEntity> findByModules(final List<Integer> idModule) {
        return repository.findAllByModules(idModule);
    }

    public List<CoursEntity> findByListIdCours(final List<String> idsCours) {
        return repository.findAllByFromListIdCours(idsCours);
    }

    public List<CoursEntity> findByFormations(final List<String> codesFormation) {
        return repository.findByFormations(codesFormation);
    }

    private List<CoursComplet> convertRawListToCoursComplet(final List<Object[]> listObj) {
        final List<CoursComplet> coursComplets = new ArrayList<>();
        listObj.forEach(raw -> {
            final CoursComplet complet = new CoursComplet();
            complet.setIdCours((String) raw[0]);
            complet.setIdModule((Integer) raw[1]);
            complet.setLibelleFormation((String) raw[2]);
            complet.setLibelleFormationLong((String) raw[3]);
            complet.setLibelleCours((String) raw[4]);
            complet.setLibelleModule((String) raw[5]);
            complet.setCodeLieu((Integer) raw[6]);
            complet.setDebut((Date) raw[7]);
            complet.setFin((Date) raw[8]);
            coursComplets.add(complet);
        });
        return coursComplets;
    }
}
