package fr.nantes.eni.alterplanning.dao.sqlserver.repository;

import fr.nantes.eni.alterplanning.dao.sqlserver.entity.CoursEntity;
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
public interface ICoursRepository extends CrudRepository<CoursEntity, String> {

    @SuppressWarnings("JpaQlInspection")
    @Query("SELECT c.idCours, m.idModule, form.libelleCourt, form.libelleLong, c.libelleCours ,m.libelle, " +
            "c.codeLieu, c.debut, c.fin, c.dureeReelleEnHeures, form.codeFormation " +
            "FROM CoursEntity c " +
            "LEFT OUTER JOIN ModuleEntity m on c.idModule = m.idModule " +
            "LEFT OUTER JOIN ModuleParUniteEntity mpu on m.idModule = mpu.idModule " +
            "LEFT OUTER JOIN UniteParFormationEntity upf on mpu.idUnite = upf.id " +
            "LEFT OUTER JOIN UniteFormationEntity uf on upf.idUniteFormation = uf.idUniteFormation " +
            "LEFT OUTER JOIN FormationEntity form on upf.codeFormation = form.codeFormation ")
    List<Object[]> findAllCoursComplets();

    @SuppressWarnings("JpaQlInspection")
    @Query("SELECT c.idCours, m.idModule, form.libelleCourt, form.libelleLong, c.libelleCours ,m.libelle, " +
            "c.codeLieu, c.debut, c.fin, c.dureeReelleEnHeures, form.codeFormation " +
            "FROM CoursEntity c " +
            "LEFT OUTER JOIN ModuleEntity m on c.idModule = m.idModule " +
            "LEFT OUTER JOIN ModuleParUniteEntity mpu on m.idModule = mpu.idModule " +
            "LEFT OUTER JOIN UniteParFormationEntity upf on mpu.idUnite = upf.id " +
            "LEFT OUTER JOIN UniteFormationEntity uf on upf.idUniteFormation = uf.idUniteFormation " +
            "LEFT OUTER JOIN FormationEntity form on upf.codeFormation = form.codeFormation " +
            "WHERE c.codeLieu IN (:codes)")
    List<Object[]> findAllCoursCompletsByLieux(@Param("codes") List<Integer> codesLieu);

    @SuppressWarnings("JpaQlInspection")
    @Query("SELECT c.idCours, m.idModule, form.libelleCourt, form.libelleLong, c.libelleCours ,m.libelle, " +
            "c.codeLieu, c.debut, c.fin, c.dureeReelleEnHeures, form.codeFormation " +
            "FROM CoursEntity c " +
            "LEFT OUTER JOIN ModuleEntity m on c.idModule = m.idModule " +
            "LEFT OUTER JOIN ModuleParUniteEntity mpu on m.idModule = mpu.idModule " +
            "LEFT OUTER JOIN UniteParFormationEntity upf on mpu.idUnite = upf.id " +
            "LEFT OUTER JOIN UniteFormationEntity uf on upf.idUniteFormation = uf.idUniteFormation " +
            "LEFT OUTER JOIN FormationEntity form on upf.codeFormation = form.codeFormation " +
            "WHERE c.idCours IN (:ids)")
    List<Object[]> findAllCoursCompletsByIds(@Param("ids") List<String> ids);

    @SuppressWarnings("JpaQlInspection")
    @Query("SELECT c FROM CoursEntity c " +
            "WHERE c.codePromotion = :codePromotion")
    List<CoursEntity> findAllByPromotion(@Param("codePromotion") String codePromotion);

    @SuppressWarnings("JpaQlInspection")
    @Query("SELECT c FROM CoursEntity c " +
            "WHERE c.codeLieu = :codeLieu")
    List<CoursEntity> findAllByLieu(@Param("codeLieu") Integer codeLieu);

    @SuppressWarnings("JpaQlInspection")
    @Query("SELECT c FROM CoursEntity c " +
            "WHERE c.idModule = :idModule")
    List<CoursEntity> findAllByModule(@Param("idModule") Integer idModule);

    @SuppressWarnings("JpaQlInspection")
    @Query("SELECT c FROM CoursEntity c " +
            "WHERE c.idCours IN (:ids)")
    List<CoursEntity> findAllByFromListIdCours(@Param("ids") List<String> ids);

    @SuppressWarnings("JpaQlInspection")
    @Query("SELECT c FROM CoursEntity c " +
            "WHERE c.codeLieu IN (:codes)")
    List<CoursEntity> findAllByLieux(@Param("codes") List<Integer> codesLieu);

    @SuppressWarnings("JpaQlInspection")
    @Query("SELECT c FROM CoursEntity c " +
            "LEFT OUTER JOIN ModuleEntity m on c.idModule = m.idModule " +
            "LEFT OUTER JOIN ModuleParUniteEntity mpu on m.idModule = mpu.idModule " +
            "LEFT OUTER JOIN UniteParFormationEntity upf on mpu.idUnite = upf.id " +
            "LEFT OUTER JOIN UniteFormationEntity uf on upf.idUniteFormation = uf.idUniteFormation " +
            "LEFT OUTER JOIN FormationEntity form on upf.codeFormation = form.codeFormation " +
            "WHERE form.codeFormation IN (:codesFormation)")
    List<CoursEntity> findByFormations(@Param("codesFormation") List<String> codesFormation);

    @SuppressWarnings("JpaQlInspection")
    @Query("SELECT c FROM CoursEntity c " +
            "WHERE c.idModule IN (:ids)")
    List<CoursEntity> findAllByModules(@Param("ids") List<Integer> ids);
}
