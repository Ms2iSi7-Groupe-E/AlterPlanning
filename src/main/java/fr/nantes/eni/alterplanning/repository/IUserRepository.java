package fr.nantes.eni.alterplanning.web.repository;

import fr.nantes.eni.alterplanning.web.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by ughostephan on 23/06/2017.
 */
@Repository
public interface IUserRepository extends JpaRepository<UserEntity, String> {

    UserEntity findByEmailAndEnabledTrue(String email);

    int countByEmail(String email);

    /*@SuppressWarnings("JpaQlInspection")
    @Query("SELECT COUNT(u) FROM UserEntity u " +
            "WHERE u.email = :email")
    int countUserForEmail(@Param("email") String email);*/
}
