package com.daqem.yamlconfig.impl.config;

import com.daqem.yamlconfig.api.config.ConfigType;
import com.daqem.yamlconfig.api.config.IConfig;
import com.daqem.yamlconfig.api.config.IConfigManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigManager implements IConfigManager {

    Map<String, Map<String, IConfig>> configs = new HashMap<>();

    @Override
    public List<IConfig> getAllConfigs() {
        return configs.values().stream().flatMap(m -> m.values().stream()).toList();
    }

    @Override
    public List<IConfig> getAllCommonConfigs() {
        return getAllConfigs().stream()
                .filter(c -> c.getType() == ConfigType.COMMON)
                .toList();
    }

    @Override
    public List<IConfig> getAllServerAndCommonConfigs() {
        return getAllConfigs().stream()
                .filter(c -> c.getType() == ConfigType.COMMON || c.getType() == ConfigType.SERVER)
                .toList();
    }

    @Override
    public List<IConfig> getAllClientConfigs() {
        return getAllConfigs().stream()
                .filter(c -> c.getType() == ConfigType.CLIENT)
                .toList();
    }

    @Override
    public List<IConfig> getConfigs(String modId) {
        return configs.getOrDefault(modId, new HashMap<>()).values().stream().toList();
    }

    @Override
    public IConfig getConfig(String modId, String configName) {
        return configs.getOrDefault(modId, new HashMap<>()).get(configName);
    }

    @Override
    public void registerConfig(IConfig config) {
        configs.computeIfAbsent(config.getModId(), k -> new HashMap<>()).put(config.getName(), config);
    }

    @Override
    public void unregisterConfig(String modId, String configName) {
        configs.getOrDefault(modId, new HashMap<>()).remove(configName);
    }

    @Override
    public void unregisterAllConfigs(String modId) {
        configs.remove(modId);
    }

    @Override
    public void reloadSyncedConfigs() {
        getAllCommonConfigs().stream()
                .filter(IConfig::isSynced)
                .forEach(IConfig::load);
    }
}
