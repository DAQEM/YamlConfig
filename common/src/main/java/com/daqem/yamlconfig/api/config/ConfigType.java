package com.daqem.yamlconfig.api.config;

import com.daqem.yamlconfig.api.config.serializer.IConfigSerializer;
import com.daqem.yamlconfig.impl.config.ClientConfig;
import com.daqem.yamlconfig.impl.config.CommonConfig;
import com.daqem.yamlconfig.impl.config.ServerConfig;

/**
 * Represents the type of configuration, determining its side (client, common, server) and serializer.
 */
public enum ConfigType {
    CLIENT(new ClientConfig.Serializer()),
    COMMON(new CommonConfig.Serializer()),
    SERVER(new ServerConfig.Serializer());

    private final IConfigSerializer<? extends IConfig> serializer;

    ConfigType(IConfigSerializer<? extends IConfig> serializer) {
        this.serializer = serializer;
    }
    /**
     * Gets the serializer associated with this configuration type.
     *
     * @return The {@link IConfigSerializer} for this config type.
     */
    public IConfigSerializer<? extends IConfig> getSerializer() {
        return serializer;
    }
}