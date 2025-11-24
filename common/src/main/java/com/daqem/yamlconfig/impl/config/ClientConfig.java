package com.daqem.yamlconfig.impl.config;

import com.daqem.yamlconfig.api.config.ConfigExtension;
import com.daqem.yamlconfig.api.config.ConfigType;
import com.daqem.yamlconfig.api.config.entry.IStackConfigEntry;
import net.minecraft.client.Minecraft;

import java.nio.file.Path;
import java.util.Map;

public class ClientConfig extends BaseConfig {

    public ClientConfig(String modId, String name, ConfigExtension extension, Path path, IStackConfigEntry context) {
        super(modId, name, extension, ConfigType.CLIENT, path, context);
        try {
            //noinspection ResultOfMethodCallIgnored
            Minecraft.getInstance();
        } catch (Exception e) {
            throw new IllegalStateException("ClientConfig can only be instantiated on the client side");
        }
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
