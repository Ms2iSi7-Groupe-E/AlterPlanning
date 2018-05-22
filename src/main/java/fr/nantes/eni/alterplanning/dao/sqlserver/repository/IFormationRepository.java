package fr.nantes.eni.alterplanning.dao.sqlserver.repository;

import fr.nantes.eni.alterplanning.dao.sqlserver.entity.FormationEntity;
import fr.nantes.eni.alterplanning.dao.sqlserver.entity.ModuleEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ughostephan on 23/06/2017.
 */
@Repository
public interface IFormationRepository extends CrudRepository<FormationEntity, String> {

    @SuppressWarnings("JpaQlInspection")
    @Query("SELECT f FROM FormationEntity f WHERE f.codeTitre = :codeTitre")
    List<FormationEntity> findByTitre(@Param("codeTitre") String codeTitre);
}
