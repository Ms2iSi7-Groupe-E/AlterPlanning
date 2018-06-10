package fr.nantes.eni.alterplanning.dao.sqlserver.repository;

import fr.nantes.eni.alterplanning.dao.sqlserver.entity.CoursEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ughostephan on 23/06/2017.
 */
@Repository
public interface ICoursRepository extends CrudRepository<CoursEntity, String> {

    @SuppressWarnings("JpaQlInspection")
    @Query("SELECT c FROM CoursEntity c " +
            "WHERE c.codePromotion= :codePromotion")
    List<CoursEntity> findAllByPromotion(@Param("codePromotion") String codePromotion);

    @SuppressWarnings("JpaQlInspection")
    @Query("SELECT c FROM CoursEntity c " +
            "WHERE c.codeLieu= :codeLieu")
    List<CoursEntity> findAllByLieu(@Param("codeLieu") Integer codeLieu);

    @SuppressWarnings("JpaQlInspection")
    @Query("SELECT c FROM CoursEntity c " +
            "WHERE c.idModule= :idModule")
    List<CoursEntity> findAllByModule(@Param("idModule") Integer idModule);

    @SuppressWarnings("JpaQlInspection")
    @Query("SELECT c FROM CoursEntity c " +
            "WHERE c.idCours IN (:ids)")
    List<CoursEntity> findAllByFromListIdCours(@Param("ids") List<String> ids);
}
