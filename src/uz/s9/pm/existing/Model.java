package uz.s9.pm.existing;

import uz.s9.pm.generation.tools.Generation;
import uz.s9.pm.generation.tools.GenerationType;

/**
 * @author Ramses de Norre
 *         Date: 25/05/12
 *         Time: 22:16
 */
public abstract class Model {

    @Generation(type = GenerationType.FIRE_PROPERTY_CHANGE)
    public void firePropertyChange(final String propertyName,
                                   final Object oldValue,
                                   final Object newValue) {
        // NO-OP;
    }

}
