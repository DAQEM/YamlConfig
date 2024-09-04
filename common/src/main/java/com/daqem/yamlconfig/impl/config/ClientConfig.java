package com.daqem.yamlconfig.impl.config;

import com.daqem.yamlconfig.api.config.ConfigExtension;
import com.daqem.yamlconfig.api.config.ConfigType;
import com.daqem.yamlconfig.api.config.entry.IStackConfigEntry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.nio.file.Path;
import java.util.Map;

public class ClientConfig extends BaseConfig {

    @Environment(EnvType.CLIENT)
    public ClientConfig(String modId, String name, ConfigExtension extension, Path path, IStackConfigEntry context) {
        super(modId, name, extension, ConfigType.CLIENT, path, context);
    }

    @Override
    public void sync(Map<String, ?> data) {
        throw new UnsupportedOperationException("Client config cannot be synced");
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
