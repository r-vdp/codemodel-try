package uz.s9.pm.existing;

import uz.s9.pm.generation.tools.Generation;
import uz.s9.pm.generation.tools.GenerationType;

/**
 * @author Ramses de Norre
 *         Date: 25/05/12
 *         Time: 15:09
 */
public abstract class AbstractModel<E extends AbstractEntity>
        extends Model {

    private E entity;

    public AbstractModel(final E entity) {
        this.entity = entity;
    }

    @Generation(type = GenerationType.GET_ENTITY)
    public E getEntity() {
        return entity;
    }

    @Generation(type = GenerationType.SET_ENTITY)
    public void setEntity(final E entity) {
        this.entity = entity;
    }

}
