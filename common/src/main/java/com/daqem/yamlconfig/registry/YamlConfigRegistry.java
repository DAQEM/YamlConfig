package com.daqem.yamlconfig.registry;

import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.api.config.entry.type.IConfigEntryType;
import com.daqem.yamlconfig.impl.config.entry.type.ConfigEntryTypes;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public interface YamlConfigRegistry {

    ResourceKey<Registry<IConfigEntryType<?, ?>>> CONFIG_ENTRY_KEY = ResourceKey.createRegistryKey(YamlConfig.getId("config_entry"));

    Registry<IConfigEntryType<?, ?>> CONFIG_ENTRY = new MappedRegistry<>(CONFIG_ENTRY_KEY, Lifecycle.experimental(), false);

    static void init() {
        ConfigEntryTypes.init();
    }
}
