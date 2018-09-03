package fr.nantes.eni.alterplanning.dao.mysql.repository;

import fr.nantes.eni.alterplanning.dao.mysql.entity.IndependantModuleEntity;
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
public interface IIndependantModuleRepository extends CrudRepository<IndependantModuleEntity, Integer> {

    @SuppressWarnings("JpaQlInspection")
    @Query("SELECT im FROM IndependantModuleEntity im " +
            "WHERE im.id IN (:ids)")
    List<IndependantModuleEntity> findByListId(@Param("ids") List<Integer> ids);
}
