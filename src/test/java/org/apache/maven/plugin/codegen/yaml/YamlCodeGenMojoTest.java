package org.apache.maven.plugin.codegen.yaml;

import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.Assumptions;

import java.io.File;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.*;

public class YamlCodeGenMojoTest extends AbstractMojoTestCase {

    public void testBasicUsageScenario() {
        final File pom = getTestFile("src/test/resources/fixtures/main/basic-usage/pom.xml");

        assumeThatCode(() -> lookupMojo(YamlCodeGenMojo.DEFAULT_GOAL, pom).execute())
                .doesNotThrowAnyException();

        assertThat(getTestFile("target/generated-sources/java/com/example/basic-usage/Company.java"))
                .hasSameContentAs(getTestFile("src/test/resources/fixtures/main/basic-usage/Company.java"));
    }

    public void testRenderTemplateWithBuiltInVars() {
        final File pom = getTestFile("src/test/resources/fixtures/main/basic-00/pom.xml");

        assumeThatCode(() -> lookupMojo(YamlCodeGenMojo.DEFAULT_GOAL, pom).execute())
                .doesNotThrowAnyException();

        assertThat(getTestFile("target/generated-sources/java/com/example/basic-00/built-in-var.txt"))
                .hasSameContentAs(getTestFile("src/test/resources/fixtures/main/basic-00/built-in-var.txt"));

    }

    public void testRenderTemplateWithBasicSettings() {
        final File pom = getTestFile("src/test/resources/fixtures/main/basic-01/pom.xml");

        assumeThatCode(() -> lookupMojo(YamlCodeGenMojo.DEFAULT_GOAL, pom).execute())
                .doesNotThrowAnyException();

        assertThat(getTestFile("target/generated-sources/java/com/example/one/pojo1.txt"))
                .hasSameContentAs(getTestFile("src/test/resources/fixtures/main/basic-01/pojo1.txt"));

        assertThat(getTestFile("target/generated-sources/java/com/example/two/pojo2.txt"))
                .hasSameContentAs(getTestFile("src/test/resources/fixtures/main/basic-01/pojo2.txt"));
    }

    public void testThrowWhenNoModelsConfiguredToRender() {
        final File pom = getTestFile("src/test/resources/fixtures/main/basic-02/pom.xml");

        assertThatThrownBy(() -> lookupMojo(YamlCodeGenMojo.DEFAULT_GOAL, pom).execute())
                .hasMessage("No models configured. Please, either add one or remove plugin declaration.");
    }

    public void testModelPathIsAvailableInFreeMarkerTemplate() {
        final File pom = getTestFile("src/test/resources/fixtures/main/basic-03/pom.xml");

        assumeThatCode(() -> lookupMojo(YamlCodeGenMojo.DEFAULT_GOAL, pom).execute())
                .doesNotThrowAnyException();


        assertThat(getTestFile("target/generated-sources/java/com/example/three/pojo3.txt"))
                .hasSameContentAs(getTestFile("src/test/resources/fixtures/main/basic-03/pojo.txt"));
    }
}