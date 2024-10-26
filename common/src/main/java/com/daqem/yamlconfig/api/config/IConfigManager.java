package com.daqem.yamlconfig.api.config;

import java.util.List;

public interface IConfigManager {

    List<IConfig> getAllConfigs();

    List<IConfig> getAllCommonConfigs();

    List<IConfig> getAllServerAndCommonConfigs();

    List<IConfig> getAllClientConfigs();

    List<IConfig> getConfigs(String modId);

    IConfig getConfig(String modId, String configName);

    void registerConfig(IConfig config);

    void unregisterConfig(String modId, String configName);

    void unregisterAllConfigs(String modId);

    void reloadSyncedConfigs();
}
