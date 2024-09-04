package com.daqem.yamlconfig.yaml;

import com.daqem.yamlconfig.api.config.IConfig;
import org.snakeyaml.engine.v2.api.YamlOutputStreamWriter;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class YamlFileWriter extends YamlOutputStreamWriter {

    private final IConfig config;

    public YamlFileWriter(IConfig config) throws FileNotFoundException {
        super(new FileOutputStream(new File(config.getPath().toFile(), config.getName() + config.getExtension().getExtension())), StandardCharsets.UTF_8);
        this.config = config;
    }

    @Override
    public void processIOException(IOException e) {
        throw new RuntimeException("Failed to save config file: " + config.getName() + config.getExtension().getExtension(), e);
    }
}
