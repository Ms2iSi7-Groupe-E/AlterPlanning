package fr.nantes.eni.alterplanning.dao.mysql.repository;

import fr.nantes.eni.alterplanning.dao.mysql.entity.ModuleRequirementEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IModuleRequirementRepository extends CrudRepository<ModuleRequirementEntity, Integer> {

    @SuppressWarnings("JpaQlInspection")
    @Query("SELECT m FROM ModuleRequirementEntity m " +
            "WHERE m.moduleId = :idModule")
    List<ModuleRequirementEntity> findByModule(@Param("idModule") int idModule);

    @SuppressWarnings("JpaQlInspection")
    @Query("SELECT COUNT(m) FROM ModuleRequirementEntity m " +
            "WHERE m.moduleId = :moduleId " +
            "AND m.requiredModuleId = :requiredModuleId " +
            "AND m.or = :isOr")
    int countForUniqueConstraint(@Param("moduleId") int moduleId,
                                 @Param("requiredModuleId") int requiredModuleId,
                                 @Param("isOr") boolean isOr);
}
