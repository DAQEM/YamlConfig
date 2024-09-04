package com.daqem.yamlconfig.impl.config;

import com.daqem.yamlconfig.api.config.ConfigExtension;
import com.daqem.yamlconfig.api.config.ConfigType;
import com.daqem.yamlconfig.api.config.entry.IStackConfigEntry;

import java.nio.file.Path;
import java.util.Map;

public class ServerConfig extends BaseConfig {

    public ServerConfig(String modId, String name, ConfigExtension extension, Path path, IStackConfigEntry context) {
        super(modId, name, extension, ConfigType.SERVER, path, context);
    }

    @Override
    public void sync(Map<String, ?> data) {
        throw new UnsupportedOperationException("Server config cannot be synced");
    }

    @Override
    public boolean isSynced() {
        return false;
    }

    @Override
    public void setSynced(boolean synced) {
        super.setSynced(false);
    }
}
