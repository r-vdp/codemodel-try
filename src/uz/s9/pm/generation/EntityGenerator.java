package uz.s9.pm.generation;

import uz.s9.pm.existing.AbstractEntity;

/**
 * @author Ramses de Norre
 *         Date: 26/05/12
 *         Time: 00:20
 */
public interface EntityGenerator {

    final static Class<AbstractEntity> ABSTR_ENTITY = AbstractEntity.class;

    final static String ENTITY_IFACE_SUFFIX = "Entity";

    final static String ENTITY_SUFFIX = ENTITY_IFACE_SUFFIX + "DB";

    void generate(Config config, ModelGenerator modelGenerator) throws Exception;

}
