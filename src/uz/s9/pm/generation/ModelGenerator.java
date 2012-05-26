package uz.s9.pm.generation;

import com.sun.codemodel.JClass;
import com.sun.codemodel.JMethod;

/**
 * @author Ramses de Norre
 *         Date: 25/05/12
 *         Time: 22:58
 */
public interface ModelGenerator {
    String PROPERTY_SUFFIX = "_PROPERTY";

    void init(JClass entityType) throws Exception;

    void addProperty(String propertyName,
                     JMethod entityGetter,
                     JMethod entitySetter);
}
