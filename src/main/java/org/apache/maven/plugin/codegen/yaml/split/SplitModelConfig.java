package org.apache.maven.plugin.codegen.yaml.split;

import lombok.Data;

import java.io.File;

@Data
public class SplitModelConfig {

    private File file;

    private String rootPath;

    private File output;
}
