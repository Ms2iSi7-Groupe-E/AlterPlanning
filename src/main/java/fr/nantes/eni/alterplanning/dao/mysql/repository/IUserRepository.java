package fr.nantes.eni.alterplanning.dao.mysql.repository;

import fr.nantes.eni.alterplanning.dao.mysql.entity.UserEntity;
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
public interface IUserRepository extends CrudRepository<UserEntity, Integer> {

    @SuppressWarnings("JpaQlInspection")
    @Query("SELECT COUNT(u) FROM UserEntity u " +
            "WHERE u.email = :email")
    int countByEmail(@Param("email") String email);

    @SuppressWarnings("JpaQlInspection")
    @Query("SELECT u FROM UserEntity u " +
            "WHERE u.email = :email " +
            "AND u.active = TRUE")
    UserEntity findByEmailAndActive(@Param("email") String email);

    @SuppressWarnings("JpaQlInspection")
    @Query("SELECT u FROM UserEntity u " +
            "WHERE u.id IN (:ids)")
    List<UserEntity> findAllByFromListIdUser(@Param("ids") List<Integer> ids);
}
