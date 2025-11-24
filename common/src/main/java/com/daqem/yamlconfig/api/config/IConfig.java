package com.daqem.yamlconfig.api.config;

import java.nio.file.Path;
import java.util.Map;

import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.config.entry.IStackConfigEntry;

import net.minecraft.network.chat.Component;

/**
 * Represents a configuration file.
 */
public interface IConfig {

    /**
     * Loads the configuration from the file system.
     */
    void load();

    /**
     * Saves the configuration to the file system.
     */
    void save();

    /**
     * Gets the mod ID associated with this configuration.
     *
     * @return The mod ID.
     */
    String getModId();

    /**
     * Gets the name of the configuration.
     *
     * @return The configuration name.
     */
    String getName();

    /**
     * Gets the file extension of the configuration.
     *
     * @return The {@link ConfigExtension}.
     */
    ConfigExtension getExtension();

    /**
     * Gets the type of the configuration.
     *
     * @return The {@link ConfigType}.
     */
    ConfigType getType();

    /**
     * Gets the file path of the configuration.
     *
     * @return The {@link Path} to the configuration file.
     */
    Path getPath();

    /**
     * Gets the root context of the configuration.
     *
     * @return The root {@link IStackConfigEntry}.
     */
    IStackConfigEntry getContext();

    /**
     * Gets all entries in the configuration.
     *
     * @return A map of config keys to {@link IConfigEntry} instances.
     */
    Map<String, IConfigEntry<?>> getEntries();

    /**
     * Gets all entries that should be synced to the client.
     *
     * @return A map of syncable config keys to {@link IConfigEntry} instances.
     */
    Map<String, IConfigEntry<?>> getSyncEntries();

    /**
     * Syncs the configuration with the provided data.
     *
     * @param data A map of data to sync.
     */
    void sync(Map<String, ?> data);

    /**
     * Checks if the configuration has been synced.
     *
     * @return True if synced, false otherwise.
     */
    boolean isSynced();

    /**
     * Sets the synced state of the configuration.
     *
     * @param synced True if synced, false otherwise.
     */
    void setSynced(boolean synced);

    /**
     * Gets the display name of the configuration.
     *
     * @return The display name as a {@link Component}.
     */
    Component getDisplayName();

    /**
     * Gets the mod name associated with this configuration.
     *
     * @return The mod name as a {@link Component}.
     */
    Component getModName();

    /**
     * Updates the entries in the configuration.
     *
     * @param entries A map of new entries to update.
     */
    void updateEntries(Map<String, IConfigEntry<?>> entries);
}
