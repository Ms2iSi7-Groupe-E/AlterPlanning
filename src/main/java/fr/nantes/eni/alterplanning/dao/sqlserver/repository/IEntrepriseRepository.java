package fr.nantes.eni.alterplanning.dao.sqlserver.repository;

import fr.nantes.eni.alterplanning.dao.sqlserver.entity.EntrepriseEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ughostephan on 23/06/2017.
 */
@Repository
public interface IEntrepriseRepository extends CrudRepository<EntrepriseEntity, Integer> {

    @SuppressWarnings("JpaQlInspection")
    @Query("SELECT ent FROM EntrepriseEntity ent " +
            "INNER JOIN StagiaireParEntrepriseEntity spe on ent.codeEntreprise = spe.codeEntreprise " +
            "INNER JOIN StagiaireEntity sta on spe.codeStagiaire = sta.codeStagiaire " +
            "WHERE sta.codeStagiaire = :codeStagiaire")
    List<EntrepriseEntity> findByStagiaire(@Param("codeStagiaire") Integer codeStagiaire);
}
