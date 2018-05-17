package fr.nantes.eni.alterplanning.dao.sqlserver.repository;

import fr.nantes.eni.alterplanning.dao.sqlserver.entity.ModuleEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ughostephan on 23/06/2017.
 */
@Repository
public interface IModuleRepository extends CrudRepository<ModuleEntity, Integer> {

    @SuppressWarnings("JpaQlInspection")
    @Query("SELECT m FROM ModuleEntity m " +
            "LEFT OUTER JOIN ModuleParUniteEntity mpu on m.idModule = mpu.idModule " +
            "LEFT OUTER JOIN UniteParFormationEntity upf on mpu.idUnite = upf.id " +
            "LEFT OUTER JOIN UniteFormationEntity uf on upf.idUniteFormation = uf.idUniteFormation " +
            "LEFT OUTER JOIN FormationEntity form on upf.codeFormation = form.codeFormation " +
            "WHERE form.codeFormation = :codeFormation")
    List<ModuleEntity> findByFormation(@Param("codeFormation") String codeFormation);
}
