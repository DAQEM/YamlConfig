package com.daqem.yamlconfig.impl.config;

import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.api.config.ConfigExtension;
import com.daqem.yamlconfig.api.config.ConfigType;
import com.daqem.yamlconfig.api.config.entry.IStackConfigEntry;
import dev.architectury.utils.EnvExecutor;
import net.fabricmc.api.EnvType;

import java.nio.file.Path;
import java.util.Map;

public class ClientConfig extends BaseConfig {

    public ClientConfig(String modId, String name, ConfigExtension extension, Path path, IStackConfigEntry context) {
        super(modId, name, extension, ConfigType.CLIENT, path, context);
        EnvExecutor.runInEnv(EnvType.SERVER, () -> {
            YamlConfig.LOGGER.error("Client config cannot be created on the server side. Contact the author of the mod: " + modId + ", to fix this issue.");
            System.exit(1);
            return null;
        });
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
