package uz.s9.pm.generation;

import com.sun.codemodel.internal.JClass;
import com.sun.codemodel.internal.JMethod;
import uz.s9.pm.existing.AbstractModel;
import uz.s9.pm.existing.ModelFactory;

/**
 * @author Ramses de Norre
 *         Date: 25/05/12
 *         Time: 22:58
 */
public interface ModelGenerator {

    final static Class<AbstractModel> ABSTR_MODEL = AbstractModel.class;

    final static Class<ModelFactory> ABSTR_MODEL_FACTORY = ModelFactory.class;

    final static String PROPERTY_SUFFIX = "_PROPERTY";

    final static String MODEL_SUFFIX = "Model";

    final static String FACTORY_SUFFIX = "Factory";

    void init(JClass entityType) throws Exception;

    void addProperty(String propertyName,
                     JMethod entityGetter,
                     JMethod entitySetter);
}
