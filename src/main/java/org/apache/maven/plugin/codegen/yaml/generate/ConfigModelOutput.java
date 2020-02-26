package org.apache.maven.plugin.codegen.yaml.generate;

import lombok.Data;

import java.io.File;
import java.util.Map;

@Data
public class ConfigModelOutput {

    private File tmpl;

    private File dst;

    private Map<String, Object> tmplVars;
}
