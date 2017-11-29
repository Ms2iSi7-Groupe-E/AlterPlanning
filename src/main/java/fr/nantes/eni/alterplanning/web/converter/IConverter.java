package fr.nantes.eni.alterplanning.web.converter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ughostephan on 23/06/2017.
 *
 * @param <Bean>   the type parameter
 * @param <Entity> the type parameter
 */
public interface IConverter<Bean, Entity> {

    /**
     * Bean To entity.
     *
     * @param bean the bean
     * @return the entity
     */
    Entity toEntity(final Bean bean);

    /**
     * Entity To bean.
     *
     * @param entity the entity
     * @return the bean
     */
    Bean toBean(final Entity entity);

    /**
     * To entities list.
     *
     * @param beans the beans
     * @return the list of entity
     */
    default List<Entity> toEntities(final List<Bean> beans) {
        return beans
                .stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    /**
     * To beans list.
     *
     * @param entities the entities
     * @return the list of bean
     */
    default List<Bean> toBeans(final List<Entity> entities) {
        return entities
                .stream()
                .map(this::toBean)
                .collect(Collectors.toList());
    }
}
