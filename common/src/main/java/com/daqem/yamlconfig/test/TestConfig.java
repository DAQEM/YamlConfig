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

    public static IConfigEntry<Integer> testInt;
    public static IConfigEntry<String> testString;
    public static IConfigEntry<String> testString1;
    public static IConfigEntry<List<String>> testStringList;
    public static IConfigEntry<List<Integer>> testIntList;
    public static IConfigEntry<Boolean> testBoolean;
    public static IConfigEntry<Float> testFloat;
    public static IConfigEntry<Double> testDouble;
    public static IConfigEntry<Difficulty> testEnum;
    public static IConfigEntry<Map<String, Integer>> testIntegerMap;
    public static IConfigEntry<LocalDateTime> testDateTime;
    public static IConfigEntry<Item> testItem;

    public static void init() {
        ConfigBuilder builder = new ConfigBuilder("test", "test-common", ConfigExtension.YAML);

//        testInt = builder.defineInteger("testInt", 10, 0, 100);

        testString = builder.defineString("testString", "test");

        builder.push("test").withComments("This is a test stack.", "And another comment.", "Wow even a third comment.");

        testString1 = builder.defineString("testString", "test");

        builder.pop();
//
//        testStringList = builder.defineStringList("testStringList", List.of("test1", "test2", "test3"), "test\\d+");
//
//        testIntList = builder.defineIntegerList("testIntList", List.of(1, 2, 3));
//
//        testBoolean = builder.defineBoolean("testBoolean", true);
//
//        testFloat = builder.defineFloat("testFloat", 1.0F);
//
//        testDouble = builder.defineDouble("testDouble", 1.0D);
//
//        testEnum = builder.defineEnum("testEnum", Difficulty.NORMAL, Difficulty.class);
//
//        testIntegerMap = builder.defineIntegerMap("testIntegerMap", Map.of("test1", 1, "test2", 2, "test3", 3));
//
//        testDateTime = builder.defineDateTime("testDateTime", LocalDateTime.of(2021, 1, 1, 0, 0, 0));

        testItem = builder.defineRegistry("testItem", Items.STONE, BuiltInRegistries.ITEM);

        config = builder.build();
    }
}
