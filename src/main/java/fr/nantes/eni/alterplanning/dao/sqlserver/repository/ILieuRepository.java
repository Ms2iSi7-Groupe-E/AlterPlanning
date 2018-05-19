package fr.nantes.eni.alterplanning.dao.sqlserver.repository;

import fr.nantes.eni.alterplanning.dao.sqlserver.entity.LieuEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ughostephan on 23/06/2017.
 */
@Repository
public interface ILieuRepository extends CrudRepository<LieuEntity, Integer> {

    @SuppressWarnings("JpaQlInspection")
    @Query("SELECT l FROM LieuEntity l " +
            "WHERE EXISTS(SELECT 1 FROM CoursEntity c WHERE c.codeLieu = l.codeLieu)")
    List<LieuEntity> findAllTeachingCours();
}
