package fr.nantes.eni.alterplanning.dao.mysql.repository;

import fr.nantes.eni.alterplanning.dao.mysql.entity.HistoryEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ughostephan on 23/06/2017.
 */
@Repository
public interface IHistoryRepository extends CrudRepository<HistoryEntity, Integer> {

    @SuppressWarnings("JpaQlInspection")
    @Query("SELECT h FROM HistoryEntity h " +
            "ORDER BY h.createdAt DESC")
    List<HistoryEntity> findAllOrderByDate();
}
