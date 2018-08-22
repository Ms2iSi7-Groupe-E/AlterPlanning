package fr.nantes.eni.alterplanning.dao.sqlserver.repository;

import fr.nantes.eni.alterplanning.dao.sqlserver.entity.TitreEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by ughostephan on 23/06/2017.
 */
@Repository
public interface ITitreRepository extends CrudRepository<TitreEntity, String> {

    @SuppressWarnings("JpaQlInspection")
    @Query("SELECT t FROM TitreEntity t " +
            "INNER JOIN FormationEntity form on t.codeTitre = form.codeTitre " +
            "WHERE form.codeFormation = :codeFormation ")
    TitreEntity findByFormation(@Param("codeFormation") String codeFormation);
}
