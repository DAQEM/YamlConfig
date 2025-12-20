package com.daqem.yamlconfig.test;

import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.config.entry.serializer.IConfigEntrySerializer;
import com.daqem.yamlconfig.api.config.entry.type.IConfigEntryType;
import com.daqem.yamlconfig.registry.YamlConfigRegistry;
import com.daqem.yamlconfig.test.config.entry.TestConfigEntry;
import com.daqem.yamlconfig.test.config.entry.TestConfigEntrySerializer;
import com.mojang.logging.LogUtils;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;

public class TestMod {
    public static final String MOD_ID = "yamlconfig_test";

    public static IConfigEntryType<TestConfigEntry, String> TEST_ENTRY_TYPE;

    public static void init() {
        registerConfigEntryTypes();
        CommonTestConfig.init();
    }

    private static void registerConfigEntryTypes() {
        TEST_ENTRY_TYPE = register(getId("test"), new TestConfigEntrySerializer());
    }

    private static <C extends IConfigEntry<T>, T> IConfigEntryType<C, T> register(Identifier id, IConfigEntrySerializer<C, T> serializer) {
        return Registry.register(YamlConfigRegistry.CONFIG_ENTRY, id, new IConfigEntryType<>() {
            @Override
            public Identifier getId() {
                return id;
            }

            @Override
            public IConfigEntrySerializer<C, T> getSerializer() {
                return serializer;
            }
        });
    }

    public static Identifier getId(String id) {
        return Identifier.fromNamespaceAndPath(MOD_ID, id);
    }
}