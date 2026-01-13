package org.apache.maven.plugin.codegen.yaml;

import com.google.common.collect.ImmutableMap;
import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.codegen.yaml.binding.ConfigModel;
import org.apache.maven.plugin.codegen.yaml.binding.ConfigModelOutput;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.util.Collections.emptyMap;

@Mojo(name = YamlCodeGenMojo.DEFAULT_GOAL, requiresProject = false, threadSafe = true)
public class YamlCodeGenMojo extends AbstractMojo {

    public static final String DEFAULT_GOAL = "generate";
    private static final Charset DEFAULT_ENCODING = StandardCharsets.UTF_8;
    private static final Version DEFAULT_FREEMARKER_VERSION = Configuration.VERSION_2_3_34;

    @Parameter(name = "models", required = true)
    private List<ConfigModel> models = new ArrayList<>();

    public void execute() throws MojoExecutionException {
        final StopWatch started = StopWatch.createStarted();
        getLog().info("Found " + models.size() + " configured model(s).");

        validatedDeclaredModel();

        try {
            for (ConfigModel configModel : models) {

                final File modelFile = configModel.getFile();
                final Map<String, Object> modelContent = loadModel(modelFile);

                for (final ConfigModelOutput output : configModel.getOutputs()) {
                    getLog().debug("Preparing FreeMarker to render " + modelFile);

                    final File templateFile = output.getTmpl();
                    final Configuration cfg = prepareConfiguration(templateFile);
                    final Template template = cfg.getTemplate(templateFile.getName());

                    final File outputDst = output.getDst();
                    try (final Writer writer = prepareDestinationForWrite(outputDst)) {
                        final Map<String, Object> dataModel = createModel(output, modelContent, modelFile, outputDst, templateFile);
                        template.process(dataModel, writer);
                        writer.flush();
                        getLog().info("Processed  " + modelFile + " to " + outputDst);
                    }
                }
            }
            started.stop();
            getLog().info(models.size() + " model(s) processed in " + started.getTime(TimeUnit.MILLISECONDS) + " ms.");
        } catch (IOException | TemplateException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }

    private void validatedDeclaredModel() throws MojoExecutionException {
        if (models.isEmpty()) {
            throw new MojoExecutionException("No models configured. Please either add a model configuration or remove plugin declaration.");
        }
    }

    private Map<String, Object> loadModel(final File modelFile) throws IOException {
        getLog().debug("Loading model from " + modelFile);
        try (final FileInputStream io = new FileInputStream(modelFile)) {
            return new Yaml().load(io);
        }
    }

    private static Writer prepareDestinationForWrite(final File outputDst) throws IOException {
        final File outDstParent = outputDst.getParentFile();
        if (!outDstParent.exists()) {
            outDstParent.mkdirs();
        }
        return new FileWriter(outputDst);
    }

    private static Configuration prepareConfiguration(File templateFile) throws IOException {
        final Configuration cfg = new Configuration(DEFAULT_FREEMARKER_VERSION);
        cfg.setDefaultEncoding(DEFAULT_ENCODING.displayName());
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(false);
        cfg.setTemplateLoader(new FileTemplateLoader(templateFile.getParentFile()));
        return cfg;
    }

    private static Map<String, Object> createModel(ConfigModelOutput output, Map<String, Object> modelContent,
                                                   File modelFile, File outputDst, File templateFile) {
        final Map<String, Object> root = new HashMap<>();

        root.put("model",
                ImmutableMap.of(
                        "content", modelContent,
                        "file", modelFile
                )
        );
        root.put("output",
                ImmutableMap.of(
                        "file", new File(outputDst.getPath())
                )
        );
        root.put("tmpl", ImmutableMap.of(
                "file", templateFile,
                "vars", ObjectUtils.getIfNull(output.getTmplVars(), emptyMap())
        ));
        return root;
    }

}