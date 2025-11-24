package com.daqem.yamlconfig.impl.format.yaml;

import org.snakeyaml.engine.v2.api.StreamDataWriter;

import java.io.IOException;
import java.io.Writer;

public class YamlWriterWrapper implements StreamDataWriter {

    private final Writer writer;

    public YamlWriterWrapper(Writer writer) {
        this.writer = writer;
    }

    @Override
    public void flush() {
        try {
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException("Failed to flush YAML writer", e);
        }
    }

    @Override
    public void write(String str) {
        try {
            writer.write(str);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write string to YAML writer", e);
        }
    }

    @Override
    public void write(String str, int off, int len) {
        try {
            writer.write(str, off, len);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write chunk to YAML writer", e);
        }
    }
}