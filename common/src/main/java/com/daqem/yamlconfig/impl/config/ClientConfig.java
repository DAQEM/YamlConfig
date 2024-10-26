package com.daqem.yamlconfig.impl.config;

import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.api.config.ConfigExtension;
import com.daqem.yamlconfig.api.config.ConfigType;
import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.config.entry.IStackConfigEntry;
import com.daqem.yamlconfig.api.config.entry.IStringConfigEntry;
import com.daqem.yamlconfig.api.config.entry.serializer.IConfigEntrySerializer;
import com.daqem.yamlconfig.api.config.entry.type.IConfigEntryType;
import com.daqem.yamlconfig.api.config.serializer.IConfigSerializer;
import com.daqem.yamlconfig.impl.config.entry.type.ConfigEntryTypes;
import com.mojang.datafixers.util.Function5;
import dev.architectury.utils.EnvExecutor;
import net.fabricmc.api.EnvType;
import net.minecraft.network.RegistryFriendlyByteBuf;

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

    public static class Serializer extends BaseConfigSerializer<ClientConfig> {

        public Serializer() {
            super(ClientConfig::new);
        }
    }
}
