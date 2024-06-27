package com.daqem.yamlconfig.test;

import com.daqem.yamlconfig.api.ConfigExtension;
import com.daqem.yamlconfig.api.IConfig;
import com.daqem.yamlconfig.api.entry.IConfigEntry;
import com.daqem.yamlconfig.impl.ConfigBuilder;
import com.daqem.yamlconfig.impl.entry.*;
import com.daqem.yamlconfig.impl.entry.minecraft.RegistryConfigEntry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Difficulty;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TestConfig {

    public static IConfig config;

    public static IConfigEntry<String> testString;
    public static IConfigEntry<String> testString1;

    public static void init() {
        ConfigBuilder builder = new ConfigBuilder("test", "test-common", ConfigExtension.YAML);

        testString = builder.defineString("testString", "test");

        builder.push("test");

        testString1 = builder.defineString("testString1", "test1");

        builder.pop();

        config = builder.build();
    }
}
