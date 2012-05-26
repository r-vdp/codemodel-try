package uz.s9.pm.generation;

import com.sun.codemodel.ClassType;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JPackage;
import com.sun.codemodel.JPrimitiveType;
import com.sun.codemodel.JVar;
import uz.s9.pm.existing.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Version;
import java.util.Map;

import static com.sun.codemodel.JExpr._this;
import static com.sun.codemodel.JMod.FINAL;
import static com.sun.codemodel.JMod.NONE;
import static com.sun.codemodel.JMod.PRIVATE;
import static com.sun.codemodel.JMod.PUBLIC;
import static uz.s9.pm.generation.tools.GenerationTools.cap;
import static uz.s9.pm.generation.tools.GenerationTools.createSubPackage;

/**
 * @author Ramses de Norre
 *         Date: 24/05/12
 *         Time: 12:31
 */
public class EntityGeneratorImpl implements EntityGenerator {

    public static final Class<AbstractEntity> SUPER_ENTITY = AbstractEntity.class;
    public static final String ENTITY_IFACE_SUFFIX = "Entity";
    public static final String ENTITY_SUFFIX = ENTITY_IFACE_SUFFIX + "DB";

    private final JCodeModel codeModel;

    public EntityGeneratorImpl(final JCodeModel codeModel) {
        this.codeModel = codeModel;
    }

    @Override
    public void generate(final Config config,
                         final ModelGenerator modelGenerator)
            throws Exception {

        final JPackage pkg = createDAOPackage(config);
        final JDefinedClass iface = createInterface(pkg, config.name());
        final JDefinedClass clazz = createClass(pkg, iface, config);

        modelGenerator.init(iface);

        for (Map.Entry<String, Class<?>> property : config.properties().entrySet()) {
            addPropertyToInterface(iface, property.getKey(), property.getValue());
            addPropertyToClass(clazz, property.getKey(), property.getValue(),
                    modelGenerator);
        }
    }

    private JPackage createDAOPackage(final Config config) {
        return createSubPackage(codeModel, config.rootPkg(),
                config.entitiesPkg());
    }

    private JDefinedClass createInterface(final JPackage pkg,
                                          final String name)
            throws JClassAlreadyExistsException {
        final JDefinedClass iface = pkg._interface(cap(name) + ENTITY_IFACE_SUFFIX);
        iface._extends(codeModel.ref(SUPER_ENTITY).narrow(iface));
        return iface;
    }

    private JDefinedClass createClass(final JPackage pkg,
                                      final JDefinedClass iface,
                                      final Config config)
            throws JClassAlreadyExistsException {
        final JDefinedClass clazz = pkg._class(cap(config.name()) + ENTITY_SUFFIX);
        clazz._implements(iface);
        clazz.annotate(Entity.class);
        clazz.annotate(Table.class).param("name", config.table());

        final JFieldVar id = clazz.field(PRIVATE, Object.class, "id");
        id.annotate(Id.class);
        id.annotate(Column.class).param("name", "id");
        id.annotate(GeneratedValue.class).param("generator", "idGen");
        id.annotate(TableGenerator.class).param("name", "idGen");

        final JFieldVar version = clazz.field(PRIVATE, Long.class, "version");
        version.annotate(Version.class);
        version.annotate(Column.class).param("name", "version");

        return clazz;
    }

    private void addPropertyToClass(final JDefinedClass clazz,
                                    final String name,
                                    final Class<?> type,
                                    final ModelGenerator modelGenerator) {
        assert ClassType.CLASS.equals(clazz.getClassType());

        final JMethod getter = clazz.method(PUBLIC, type,  "get" + cap(name));
        getter.annotate(Override.class);

        final JMethod setter = clazz.method(PUBLIC, getVoid(), "set" + cap(name));
        final JVar setterParam = setter.param(FINAL, type, name);
        setter.annotate(Override.class);

        final JFieldVar field = clazz.field(PRIVATE, type, name);
        field.annotate(Column.class).param("name", name);

        getter.body()._return(field);
        setter.body().assign(_this().ref(field), setterParam);

        modelGenerator.addProperty(name, getter, setter);
    }

    private void addPropertyToInterface(final JDefinedClass iface,
                                        final String name,
                                        final Class<?> type) {
        assert ClassType.INTERFACE.equals(iface.getClassType());
        iface.method(NONE, type, "get" + cap(name));
        iface.method(NONE, getVoid(), "set" + cap(name)).param(type, name);
    }

    private JPrimitiveType getVoid() {
        return codeModel.VOID;
    }

}
