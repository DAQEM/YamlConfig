package com.daqem.yamlconfig.api;

import com.daqem.yamlconfig.api.entry.IConfigEntry;
import com.daqem.yamlconfig.api.entry.IStackConfigEntry;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public interface IConfig {

    void load();

    void save();

    String getModId();

    String getName();

    ConfigExtension getExtension();

    Path getPath();

    IStackConfigEntry getContext();

    Map<String, IConfigEntry<?>> getEntries();
}
