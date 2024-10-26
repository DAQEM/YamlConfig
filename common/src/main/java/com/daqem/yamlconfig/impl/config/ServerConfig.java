package com.daqem.yamlconfig.impl.config;

import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.api.config.ConfigExtension;
import com.daqem.yamlconfig.api.config.ConfigType;
import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.config.entry.IStackConfigEntry;
import com.daqem.yamlconfig.api.config.entry.type.IConfigEntryType;
import com.daqem.yamlconfig.api.config.serializer.IConfigSerializer;
import com.daqem.yamlconfig.impl.config.entry.type.ConfigEntryTypes;
import dev.architectury.utils.EnvExecutor;
import net.fabricmc.api.EnvType;
import net.minecraft.network.RegistryFriendlyByteBuf;

import java.nio.file.Path;
import java.util.Map;

public class ServerConfig extends BaseConfig {

    public ServerConfig(String modId, String name, ConfigExtension extension, Path path, IStackConfigEntry context) {
        super(modId, name, extension, ConfigType.SERVER, path, context);
        EnvExecutor.runInEnv(EnvType.CLIENT, () -> {
            YamlConfig.LOGGER.error("Server config cannot be created on the client side. Contact the author of the mod: " + modId + ", to fix this issue.");
            System.exit(1);
            return null;
        });
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

    public static class Serializer extends BaseConfigSerializer<ServerConfig> {

        public Serializer() {
            super(ServerConfig::new);
        }
    }
}
