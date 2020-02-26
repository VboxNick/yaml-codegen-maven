package org.apache.maven.plugin.codegen.yaml.split;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.junit.Ignore;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThatCode;

public class YamlSplitMojoTest extends AbstractMojoTestCase {


    public void testSplitUsaCase01() {
        final File pom = getTestFile("src/test/resources/fixtures/main/split-use-case-01/pom.xml");

        assumeThatCode(() -> lookupMojo(YamlSplitMojo.GOAL, pom).execute())
                .doesNotThrowAnyException();

        assertThat(getTestFile("target/generated-resources/components/Pet.yml"))
                .hasSameContentAs(getTestFile("src/test/resources/fixtures/main/split-use-case-01/Pet.yml"));

        assertThat(getTestFile("target/generated-resources/components/Pets.yml"))
                .hasSameContentAs(getTestFile("src/test/resources/fixtures/main/split-use-case-01/Pets.yml"));

        assertThat(getTestFile("target/generated-resources/components/Error.yml"))
                .hasSameContentAs(getTestFile("src/test/resources/fixtures/main/split-use-case-01/Error.yml"));
    }

    public void testSplitUsaCase02() {
        final File pom = getTestFile("src/test/resources/fixtures/main/split-use-case-02/pom.xml");

        assumeThatCode(() -> lookupMojo(YamlSplitMojo.GOAL, pom).execute())
                .doesNotThrowAnyException();

        assertThat(getTestFile("target/generated-resources/components/Pet.yml"))
                .hasSameContentAs(getTestFile("src/test/resources/fixtures/main/split-use-case-01/Pet.yml"));

        assertThat(getTestFile("target/generated-resources/components/Pets.yml"))
                .hasSameContentAs(getTestFile("src/test/resources/fixtures/main/split-use-case-01/Pets.yml"));

        assertThat(getTestFile("target/generated-resources/components/Error.yml"))
                .hasSameContentAs(getTestFile("src/test/resources/fixtures/main/split-use-case-01/Error.yml"));
    }
}
