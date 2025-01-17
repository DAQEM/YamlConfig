package com.daqem.yamlconfig.api.config;

import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.config.entry.IStackConfigEntry;
import net.minecraft.network.chat.Component;

import java.nio.file.Path;
import java.util.Map;

public interface IConfig {

    void load();

    void save();

    String getModId();

    String getName();

    ConfigExtension getExtension();

    ConfigType getType();

    Path getPath();

    IStackConfigEntry getContext();

    Map<String, IConfigEntry<?>> getEntries();

    Map<String, IConfigEntry<?>> getSyncEntries();

    void sync(Map<String, ?> data);

    boolean isSynced();

    void setSynced(boolean synced);

    Component getDisplayName();

    Component getModName();

    void updateEntries(Map<String, IConfigEntry<?>> entries);
}
