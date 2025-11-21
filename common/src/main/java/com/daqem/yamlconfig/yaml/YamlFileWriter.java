package com.daqem.yamlconfig.yaml;

import com.daqem.yamlconfig.api.config.IConfig;
import org.snakeyaml.engine.v2.api.YamlOutputStreamWriter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import java.nio.charset.StandardCharsets;

public class YamlFileWriter extends YamlOutputStreamWriter {

    private final IConfig config;

    public YamlFileWriter(IConfig config) throws FileNotFoundException {
        super(createOutputStreamSafely(config), StandardCharsets.UTF_8);
        this.config = config;
    }

    private static OutputStream createOutputStreamSafely(IConfig config) throws FileNotFoundException {
        Path fullPath = config.getPath().resolve(config.getName() + config.getExtension().getExtension());

        try {
            Files.createDirectories(fullPath.getParent());
        } catch (IOException e) {
            throw new RuntimeException("Failed to create config directory: " + fullPath.getParent(), e);
        }

        File file = fullPath.toFile();
        return new FileOutputStream(file);
    }

    @Override
    public void processIOException(IOException e) {
        throw new RuntimeException("Failed to save config file: " + config.getName() + config.getExtension().getExtension(), e);
    }
}