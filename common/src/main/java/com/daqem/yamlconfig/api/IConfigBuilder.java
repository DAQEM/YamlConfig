package com.daqem.yamlconfig.api;

import com.daqem.yamlconfig.api.entry.IConfigEntry;
import com.daqem.yamlconfig.api.exception.YamlConfigException;

import java.nio.file.Path;
import java.util.Map;

public interface IConfigBuilder {

    String getModId();

    String getName();

    ConfigExtension getExtension();

    Path getPath();

    boolean isBuilt();

    void build();

    <T extends IConfigEntry<?>> T define(T entry) throws YamlConfigException;

    void pop();

    void push(String key);
}
