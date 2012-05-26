package uz.s9.pm.generation.tools;

import com.sun.codemodel.internal.JClass;
import com.sun.codemodel.internal.JCodeModel;
import com.sun.codemodel.internal.JPackage;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Ramses de Norre
 *         Date: 25/05/12
 *         Time: 16:29
 */
public abstract class GenerationTools {

    static Method getMethod(final Class<?> clazz,
                            final GenerationType type) {
        for (Method method : clazz.getMethods()) {
            if (hasType(method, type)) {
                return method;
            }
        }
        throw new RuntimeException("Method not found!");
    }

    static Method getMethod(final JClass clazz,
                            final GenerationType type) {
        return getMethod(getClass(clazz), type);
    }

    static String getMethodName(final Class<?> clazz,
                                final GenerationType type) {
        return getMethod(clazz, type).getName();
    }

    static String getMethodName(final JClass clazz,
                                final GenerationType type) {
        return getMethod(clazz, type).getName();
    }

    static Class<?> getMethodReturnType(final Class<?> clazz,
                                        final GenerationType type) {
        return getMethod(clazz, type).getReturnType();
    }

    static Class<?> getMethodReturnType(final JClass clazz,
                                        final GenerationType type) {
        return getMethod(clazz, type).getReturnType();
    }

    static Class<?> getClass(final JClass jclass) {
        try {
            return ClassLoader.getSystemClassLoader().loadClass(
                    jclass.erasure().binaryName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Could not load class!", e);
        }
    }

    static boolean hasType(final Method method,
                           final GenerationType type) {
        return method.isAnnotationPresent(Generation.class) &&
               type.equals(method.getAnnotation(Generation.class).type());
    }

    public static String cap(final String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    public static JPackage createRootPackage(final JCodeModel codeModel,
                                             final List<String> subPkgs) {
        JPackage rootPkg = codeModel._package(subPkgs.get(0));
        for (String pkg : subPkgs.subList(1, subPkgs.size())) {
            rootPkg = rootPkg.subPackage(pkg);
        }
        return rootPkg;
    }

    public static JPackage createSubPackage(final JPackage rootPkg,
                                            final List<String> subPkgs) {
        JPackage subPkg = rootPkg;
        for (String pkg : subPkgs) {
            subPkg = subPkg.subPackage(pkg);
        }
        return subPkg;
    }

    public static JPackage createSubPackage(final JCodeModel codeModel,
                                            final List<String> rootPkgs,
                                            final List<String> subPkgs) {
        return createSubPackage(createRootPackage(codeModel, rootPkgs), subPkgs);
    }
}
