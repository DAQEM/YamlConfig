package com.daqem.yamlconfig.api.config;

import com.daqem.yamlconfig.api.config.serializer.IConfigSerializer;
import com.daqem.yamlconfig.impl.config.ClientConfig;
import com.daqem.yamlconfig.impl.config.CommonConfig;
import com.daqem.yamlconfig.impl.config.ServerConfig;

public enum ConfigType {
    CLIENT(new ClientConfig.Serializer()),
    COMMON(new CommonConfig.Serializer()),
    SERVER(new ServerConfig.Serializer());

    private final IConfigSerializer<? extends IConfig> serializer;

    ConfigType(IConfigSerializer<? extends IConfig> serializer) {
        this.serializer = serializer;
    }
    public IConfigSerializer<? extends IConfig> getSerializer() {
        return serializer;
    }
}