package uz.s9.pm.generation;

import com.sun.codemodel.JCodeModel;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ramses de Norre
 *         Date: 25/05/12
 *         Time: 22:34
 */
public class Generator {

    void generate(final Config config) throws Exception {
        final JCodeModel codeModel = new JCodeModel();
        final EntityGenerator entityGenerator =
                new EntityGeneratorImpl(codeModel);
        final ModelGenerator modelGenerator =
                new ModelGeneratorImpl(codeModel, config);

        entityGenerator.generate(config, modelGenerator);
        writeModel(codeModel);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void writeModel(final JCodeModel codeModel) throws IOException {
        final File destDir = selectDir();
        destDir.mkdir();
        codeModel.build(destDir);
    }

    private File selectDir() {
        final JFrame frame = new JFrame();
        final JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (JFileChooser.APPROVE_OPTION == chooser.showSaveDialog(frame)) {
            frame.dispose();
            return chooser.getSelectedFile();
        }
        System.exit(-1);
        return null;
    }

    public static void main(final String[] args) throws Exception {
        final List<String> rootPkg = new ArrayList<String>(3) {{
            add("uz");
            add("s9");
            add("generated");
        }};
        final Map<String, Class<?>> properties =
                new HashMap<String, Class<?>>() {{
                    put("name", String.class);
                    put("age", Integer.class);
                    put("birthdate", Date.class);
                }};
        new Generator().generate(new Config("table", "Foo", rootPkg, properties));
    }

}
