package com.daqem.yamlconfig.api.config;

import java.util.List;

/**
 * Manages the lifecycle and retrieval of configurations.
 */
public interface IConfigManager {

    /**
     * Gets all registered configurations.
     *
     * @return A list of all {@link IConfig} instances.
     */
    List<IConfig> getAllConfigs();

    /**
     * Gets all common configurations.
     *
     * @return A list of common {@link IConfig} instances.
     */
    List<IConfig> getAllCommonConfigs();

    /**
     * Gets all server and common configurations.
     *
     * @return A list of server and common {@link IConfig} instances.
     */
    List<IConfig> getAllServerAndCommonConfigs();

    /**
     * Gets all client configurations.
     *
     * @return A list of client {@link IConfig} instances.
     */
    List<IConfig> getAllClientConfigs();

    /**
     * Gets all configurations for a specific mod.
     *
     * @param modId The mod ID.
     * @return A list of {@link IConfig} instances for the specified mod.
     */
    List<IConfig> getConfigs(String modId);

    /**
     * Gets a specific configuration by mod ID and config name.
     *
     * @param modId      The mod ID.
     * @param configName The configuration name.
     * @return The {@link IConfig} instance, or null if not found.
     */
    IConfig getConfig(String modId, String configName);

    /**
     * Registers a new configuration.
     *
     * @param config The {@link IConfig} to register.
     */
    void registerConfig(IConfig config);

    /**
     * Unregisters a configuration.
     *
     * @param modId      The mod ID.
     * @param configName The configuration name.
     */
    void unregisterConfig(String modId, String configName);

    /**
     * Unregisters all configurations for a specific mod.
     *
     * @param modId The mod ID.
     */
    void unregisterAllConfigs(String modId);

    /**
     * Reloads all synced configurations.
     */
    void reloadSyncedConfigs();
}
