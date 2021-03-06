package org.apache.maven.plugin.codegen.yaml.binding;

import lombok.Data;

import java.io.File;
import java.util.List;

@Data
public class ConfigModel {

    private File file;

    private List<ConfigModelOutput> outputs;

}
