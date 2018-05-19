package fr.nantes.eni.alterplanning.dao.sqlserver.repository;

import fr.nantes.eni.alterplanning.dao.sqlserver.entity.EntrepriseEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by ughostephan on 23/06/2017.
 */
@Repository
public interface IEntrepriseRepository extends CrudRepository<EntrepriseEntity, Integer> {
}
