package uz.s9.pm.generation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Ramses de Norre
 *         Date: 25/05/12
 *         Time: 23:47
 */
public class Config {

    private final List<String> rootPkg;

    private final List<String> entitiesPkg;

    private final List<String> domainPkg;

    private final String table;

    private final String name;

    private final Map<String, Class<?>> properties;


    public Config(final String table, final String name,
                  final List<String> rootPkg,
                  final Map<String, Class<?>> properties) {
        this.table = table;
        this.name = name;
        this.rootPkg = Collections.unmodifiableList(rootPkg);
        this.properties = Collections.unmodifiableMap(properties);
        this.entitiesPkg = createUnmodifiableList("dao", "entities");
        this.domainPkg = createUnmodifiableList("domain");
    }

    private static <T> List<T> createUnmodifiableList(final T... ts) {
        return Collections.unmodifiableList(
                new ArrayList<T>(ts.length) {{
                    for (T t : ts) {
                        add(t);
                    }
                }}
        );
    }

    public List<String> rootPkg() {
        return rootPkg;
    }

    public List<String> entitiesPkg() {
        return entitiesPkg;
    }

    public List<String> domainPkg() {
        return domainPkg;
    }

    public String table() {
        return table;
    }

    public String name() {
        return name;
    }

    public Map<String, Class<?>> properties() {
        return properties;
    }

}
