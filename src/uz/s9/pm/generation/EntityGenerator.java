package uz.s9.pm.generation;

/**
 * @author Ramses de Norre
 *         Date: 26/05/12
 *         Time: 00:20
 */
public interface EntityGenerator {

    void generate(Config config, ModelGenerator modelGenerator) throws Exception;

}
