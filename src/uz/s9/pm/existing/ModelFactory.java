package uz.s9.pm.existing;

/**
 * @author Ramses de Norre
 *         Date: 25/05/12
 *         Time: 23:29
 */
public interface ModelFactory<E extends AbstractEntity<E>, M extends AbstractModel<E>> {

    M create(E entity);

}
