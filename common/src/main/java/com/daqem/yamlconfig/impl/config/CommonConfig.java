package com.daqem.yamlconfig.impl.config;

import com.daqem.yamlconfig.api.config.ConfigExtension;
import com.daqem.yamlconfig.api.config.ConfigType;
import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.config.entry.IStackConfigEntry;
import com.daqem.yamlconfig.api.config.entry.type.IConfigEntryType;
import com.daqem.yamlconfig.api.config.serializer.IConfigSerializer;
import com.daqem.yamlconfig.impl.config.entry.type.ConfigEntryTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;

import java.nio.file.Path;
import java.util.Map;

public class CommonConfig extends BaseConfig {

    public CommonConfig(String modId, String name, ConfigExtension extension, Path path, IStackConfigEntry context) {
        super(modId, name, extension, ConfigType.COMMON, path, context);
    }

    public static class Serializer extends BaseConfigSerializer<CommonConfig> {

        public Serializer() {
            super(CommonConfig::new);
        }
    }
}
