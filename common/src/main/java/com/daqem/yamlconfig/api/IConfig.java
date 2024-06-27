package com.daqem.yamlconfig.api;

import com.daqem.yamlconfig.api.entry.IStackConfigEntry;

import java.nio.file.Path;

public interface IConfig {

    void load();

    void save();

    String getModId();

    String getName();

    ConfigExtension getExtension();

    Path getPath();

    IStackConfigEntry getContext();
}
