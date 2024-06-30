package com.daqem.yamlconfig.api.config;

import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.config.entry.IStackConfigEntry;

import java.nio.file.Path;
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

    Map<String, IConfigEntry<?>> getSyncEntries();

    void sync(Map<String, ?> data);

    boolean isSynced();

    void setSynced(boolean synced);
}
