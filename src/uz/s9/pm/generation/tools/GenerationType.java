package uz.s9.pm.generation.tools;

import com.sun.codemodel.internal.JClass;

/**
 * @author Ramses de Norre
 *         Date: 25/05/12
 *         Time: 16:15
 */
public enum GenerationType {

    GET_ENTITY,
    SET_ENTITY,
    FIRE_PROPERTY_CHANGE;

    public String getMethodName(final Class<?> clazz) {
        return GenerationTools.getMethodName(clazz, this);
    }

    public String getMethodName(final JClass clazz) {
        return GenerationTools.getMethodName(clazz, this);
    }

    public Class<?> getMethodReturnType(final Class<?> clazz) {
        return GenerationTools.getMethodReturnType(clazz, this);
    }

    public Class<?> getMethodReturnType(final JClass clazz) {
        return GenerationTools.getMethodReturnType(clazz, this);
    }

}
