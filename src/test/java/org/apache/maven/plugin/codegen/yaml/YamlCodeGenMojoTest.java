package org.apache.maven.plugin.codegen.yaml;

import org.apache.maven.api.plugin.testing.InjectMojo;
import org.apache.maven.api.plugin.testing.MojoTest;
import org.apache.maven.plugin.Mojo;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assumptions.assumeThatCode;
import static org.codehaus.plexus.testing.PlexusExtension.getTestFile;

@MojoTest
public class YamlCodeGenMojoTest {

    @Test
    @InjectMojo(goal = YamlCodeGenMojo.DEFAULT_GOAL, pom = "src/test/resources/fixtures/main/basic-usage/pom.xml")
    void testBasicUsageScenario(Mojo mojo) {
        assumeThatCode(mojo::execute).doesNotThrowAnyException();

        assertThat(getTestFile("target/generated-sources/java/com/example/basic-usage/Company.java"))
                .hasSameTextualContentAs(getTestFile("src/test/resources/fixtures/main/basic-usage/Company.java"));
    }

    @Test
    @InjectMojo(goal = YamlCodeGenMojo.DEFAULT_GOAL, pom = "src/test/resources/fixtures/main/basic-00/pom.xml")
    void testRenderTemplateWithBuiltInVars(Mojo mojo) {
        assumeThatCode(mojo::execute).doesNotThrowAnyException();

        assertThat(getTestFile("target/generated-sources/java/com/example/basic-00/built-in-var.txt"))
                .hasSameTextualContentAs(getTestFile("src/test/resources/fixtures/main/basic-00/built-in-var.txt"));
    }

    @Test
    @InjectMojo(goal = YamlCodeGenMojo.DEFAULT_GOAL, pom = "src/test/resources/fixtures/main/basic-01/pom.xml")
    void testRenderTemplateWithBasicSettings(Mojo mojo) {
        assumeThatCode(mojo::execute).doesNotThrowAnyException();

        assertThat(getTestFile("target/generated-sources/java/com/example/one/pojo1.txt"))
                .hasSameTextualContentAs(getTestFile("src/test/resources/fixtures/main/basic-01/pojo1.txt"));
        assertThat(getTestFile("target/generated-sources/java/com/example/two/pojo2.txt"))
                .hasSameTextualContentAs(getTestFile("src/test/resources/fixtures/main/basic-01/pojo2.txt"));
    }

    @Test
    @InjectMojo(goal = YamlCodeGenMojo.DEFAULT_GOAL, pom = "src/test/resources/fixtures/main/basic-02/pom.xml")
    void testThrowWhenNoModelsConfiguredToRender(Mojo mojo) {
        assertThatThrownBy(mojo::execute).hasMessage("No models configured. Please either add a model configuration or remove plugin declaration.");
    }

    @Test
    @InjectMojo(goal = YamlCodeGenMojo.DEFAULT_GOAL, pom = "src/test/resources/fixtures/main/basic-03/pom.xml")
    void testModelPathIsAvailableInFreeMarkerTemplate(Mojo mojo) {
        assumeThatCode(mojo::execute).doesNotThrowAnyException();

        assertThat(getTestFile("target/generated-sources/java/com/example/three/pojo3.txt"))
                .hasSameTextualContentAs(getTestFile("src/test/resources/fixtures/main/basic-03/pojo.txt"));
    }

}