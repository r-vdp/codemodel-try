package uz.s9.pm.generation;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JPackage;
import com.sun.codemodel.JType;
import com.sun.codemodel.JVar;
import uz.s9.pm.existing.AbstractModel;
import uz.s9.pm.existing.ModelFactory;
import uz.s9.pm.generation.tools.GenerationType;

import static com.sun.codemodel.JExpr.invoke;
import static com.sun.codemodel.JMod.FINAL;
import static com.sun.codemodel.JMod.NONE;
import static com.sun.codemodel.JMod.PUBLIC;
import static com.sun.codemodel.JMod.STATIC;
import static uz.s9.pm.generation.tools.GenerationTools.cap;
import static uz.s9.pm.generation.tools.GenerationTools.createSubPackage;

/**
 * @author Ramses de Norre
 *         Date: 25/05/12
 *         Time: 15:39
 */
public class ModelGeneratorImpl implements ModelGenerator {

    public static final Class<AbstractModel> SUPER_MODEL = AbstractModel.class;
    public static final Class<ModelFactory> MODEL_FACTORY = ModelFactory.class;
    public static final String CLASS_SUFFIX = "Model";
    public static final String FACTORY_SUFFIX = "Factory";

    private final JCodeModel codeModel;
    private final JDefinedClass iface;
    private final JDefinedClass clazz;
    private final JPackage domainPkg;

    public ModelGeneratorImpl(final JCodeModel codeModel,
                              final Config config) throws Exception {
        this.codeModel = codeModel;
        domainPkg = createDomainPkg(config);
        iface = domainPkg._interface(cap(config.name()));
        clazz = domainPkg._class(cap(config.name()) + CLASS_SUFFIX);
    }

    private JPackage createDomainPkg(final Config config) {
        return createSubPackage(codeModel, config.rootPkg(), config.domainPkg());
    }

    @Override
    public void init(final JClass entityType) throws Exception {
        clazz._implements(iface);
        clazz._extends(codeModel.ref(SUPER_MODEL).narrow(entityType));

        createConstructor(entityType);
        createFactory(domainPkg, entityType);
    }

    private void createConstructor(final JType entityType) {
        final JMethod constructor = clazz.constructor(PUBLIC);
        final JVar param = constructor.param(FINAL, entityType, "entity");
        constructor.body().invoke("super").arg(param);
    }

    private void createFactory(final JPackage pkg,
                               final JClass entityType) throws Exception {
        final JMethod create = pkg._class(clazz.name() + FACTORY_SUFFIX)
                ._implements(
                        codeModel.ref(MODEL_FACTORY).narrow(entityType, clazz))
                .method(PUBLIC, clazz, "create");
        create.annotate(Override.class);
        final JVar entity = create.param(FINAL, entityType, "entity");
        create.body()._return(JExpr._new(clazz).arg(entity));
    }

    @Override
    public void addProperty(final String propertyName,
                            final JMethod entityGetter,
                            final JMethod entitySetter) {
        final JMethod getter = addPropertyGetter(entityGetter);
        final JFieldVar propertyConstant = addPropertyName(propertyName);
        addPropertySetter(entitySetter, getter, propertyConstant);
    }

    private JMethod addPropertyGetter(final JMethod method) {
        iface.method(NONE, method.type(), method.name());

        final JMethod getter = clazz.method(PUBLIC, method.type(), method.name());
        getter.annotate(Override.class);
        getter.body()._return(
                invoke(getGetEntityMethod(clazz._extends())).invoke(method));
        return getter;
    }

    private JFieldVar addPropertyName(final String propertyName) {
        return iface.field(STATIC|FINAL,
                String.class,
                propertyName.toUpperCase() + PROPERTY_SUFFIX,
                JExpr.lit(propertyName));
    }

    private void addPropertySetter(final JMethod entitySetter,
                                   final JMethod getter,
                                   final JFieldVar propertyConstant) {
        final JMethod setter =
                clazz.method(PUBLIC, codeModel.VOID, entitySetter.name());
        assert entitySetter.listParams().length == 1;
        final JVar entityParam = entitySetter.listParams()[0];
        final JVar param = setter.param(entityParam.type(), entityParam.name());
        final JBlock body = setter.body();

        final JVar old = body.decl(FINAL, param.type(), "old", invoke(getter));

        body.invoke(invoke(getGetEntityMethod(clazz._extends())), entitySetter)
                .arg(param);

        body.invoke(getFirePropertyChangeMethod(clazz._extends()))
                .arg(propertyConstant)
                .arg(old)
                .arg(invoke(getter));
    }

    private String getGetEntityMethod(final JClass clazz) {
        return GenerationType.GET_ENTITY.getMethodName(clazz);
    }

    private String getFirePropertyChangeMethod(final JClass clazz) {
        return GenerationType.FIRE_PROPERTY_CHANGE.getMethodName(clazz);
    }
}
