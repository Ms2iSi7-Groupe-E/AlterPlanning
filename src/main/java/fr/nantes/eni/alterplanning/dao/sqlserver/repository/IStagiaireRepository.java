package fr.nantes.eni.alterplanning.dao.sqlserver.repository;

import fr.nantes.eni.alterplanning.dao.sqlserver.entity.StagiaireEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ughostephan on 23/06/2017.
 */
@Repository
public interface IStagiaireRepository extends CrudRepository<StagiaireEntity, Integer> {

    @SuppressWarnings("JpaQlInspection")
    @Query("SELECT sta FROM StagiaireEntity sta " +
            "INNER JOIN StagiaireParEntrepriseEntity spe on sta.codeStagiaire = spe.codeStagiaire " +
            "INNER JOIN EntrepriseEntity ent on spe.codeEntreprise = ent.codeEntreprise " +
            "WHERE ent.codeEntreprise = :codeEntreprise")
    List<StagiaireEntity> findByEntreprise(@Param("codeEntreprise") Integer codeEntreprise);

}
