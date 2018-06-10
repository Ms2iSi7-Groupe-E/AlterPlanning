package fr.nantes.eni.alterplanning.dao.mysql.repository;

import fr.nantes.eni.alterplanning.dao.mysql.entity.ParameterEntity;
import fr.nantes.eni.alterplanning.dao.mysql.entity.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by ughostephan on 23/06/2017.
 */
@Repository
public interface IParameterRepository extends CrudRepository<ParameterEntity, String> {
}
