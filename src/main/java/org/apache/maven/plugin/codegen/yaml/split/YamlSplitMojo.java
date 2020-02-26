package org.apache.maven.plugin.codegen.yaml.split;


import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Node;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Mojo(name = YamlSplitMojo.GOAL, requiresProject = false, threadSafe = true)
public class YamlSplitMojo extends AbstractMojo {

    public static final String GOAL = "split";
    public static final Charset DEFAULT_ENCODING = StandardCharsets.UTF_8;

    @Parameter(name = "models", required = true)
    private List<SplitModelConfig> models = new ArrayList<>();


    public void execute() throws MojoExecutionException {

        final DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setPrettyFlow(true);
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        dumperOptions.setIndicatorIndent(1);
        dumperOptions.setIndent(2);
        dumperOptions.setCanonical(false);
        final Yaml yaml = new Yaml(dumperOptions);

        final StopWatch started = StopWatch.createStarted();
        getLog().info("Found " + models.size() + " configured model(s).");

        validatedDeclaredModel();

        try {
            getLog().info("Using " + DEFAULT_ENCODING + " as default encoding for writing.");
            for (final SplitModelConfig configModel : models) {

                final File modelFile = configModel.getFile();
                final Map<String, Object> modelContent = loadModel(modelFile, yaml);
                final Map<String, Object> startRoot = splitModels(configModel.getRootPath().split("\\."), modelContent, yaml);
                for (Map.Entry<String, Object> e : startRoot.entrySet()) {
                    final String modelName = e.getKey();
                    final File targetModelFile = new File(configModel.getOutput(), modelName + ".yml");
                    targetModelFile.getParentFile().mkdirs();

                    FileUtils.write(targetModelFile, yaml.dump(e.getValue()), DEFAULT_ENCODING);
                    getLog().info("Writing '" + modelName + "' to " + targetModelFile.getAbsolutePath());

                }
            }
            started.stop();
            getLog().info(models.size() + " model(s) processed in " + started.getTime(TimeUnit.MILLISECONDS) + " in ms.");
        } catch (IOException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }

    }

    private Map<String, Object> splitModels(final String[] path, final Map<String, Object> modelContent, Yaml yaml) {
        if (path.length > 0) {
            final String dump = yaml.dump(modelContent.get(path[0]));
            final Map<String, Object> o = yaml.load(dump);//todo not nice
            return splitModels(ArrayUtils.remove(path, 0), o, yaml);
        } else {
            return modelContent;
        }
    }

    private void validatedDeclaredModel() throws MojoExecutionException {
        if (models.isEmpty()) {
            throw new MojoExecutionException("No models configured. Please, either add one or remove plugin declaration.");
        }
    }

    private Map<String, Object> loadModel(final File modelFile, final Yaml yaml) throws IOException {
        getLog().debug("Loading model from " + modelFile);
        try (final FileInputStream io = new FileInputStream(modelFile)) {
            return yaml.load(io);
        }
    }

    private Writer prepareDestinationForWrite(final File outputDst) throws IOException {

        final File outDstParent = outputDst.getParentFile();
        if (!outDstParent.exists()) {
            outDstParent.mkdirs();
        }

        return new FileWriter(outputDst);

    }
}