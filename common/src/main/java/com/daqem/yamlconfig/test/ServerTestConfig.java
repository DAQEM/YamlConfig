package com.daqem.yamlconfig.test;

import com.daqem.yamlconfig.api.config.ConfigExtension;
import com.daqem.yamlconfig.api.config.ConfigType;
import com.daqem.yamlconfig.api.config.IConfig;
import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.impl.config.ConfigBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Difficulty;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class ServerTestConfig {

    public static IConfig config;

    public static IConfigEntry<Boolean> debug;

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
    public static IConfigEntry<ResourceLocation> testResourceLocation;

    public static void init() {
        ConfigBuilder builder = new ConfigBuilder("test", "test-server", ConfigExtension.YAML, ConfigType.SERVER);

        debug = builder.defineBoolean("debug", false)
                .withComments("Whether debug mode is enabled for the mod.")
                .dontSync();


        builder.push("test").withComments("This is a test stack.", "And another comment.", "Wow even a third comment.");

        testInt = builder.defineInteger("testInt", 10, 0, 100);

        builder.push("test1");

        testString = builder.defineString("testString", "test");

        builder.push("test2");

        testString1 = builder.defineString("testString", "test");

        builder.pop();

        builder.pop();

        builder.pop();

        testStringList = builder.defineStringList("testStringList", List.of("test1", "test2", "test3"),3, 10, "test\\d+");

        testIntList = builder.defineIntegerList("testIntList", List.of(1, 2, 3), 3, 10);

        testBoolean = builder.defineBoolean("testBoolean", true);

        testFloat = builder.defineFloat("testFloat", 1.0F);

        testDouble = builder.defineDouble("testDouble", 1.0D);

        testEnum = builder.defineEnum("testEnum", Difficulty.NORMAL, Difficulty.class);

        testIntegerMap = builder.defineIntegerMap("testIntegerMap", Map.of("test1", 1, "test2", 2, "test3", 3), 3, 10);

        testDateTime = builder.defineDateTime("testDateTime", LocalDateTime.of(2021, 1, 1, 0, 0, 0));

        testItem = builder.defineRegistry("testItem", Items.STONE, BuiltInRegistries.ITEM);

        testResourceLocation = builder.defineResourceLocation("testResourceLocation", ResourceLocation.fromNamespaceAndPath("minecraft", "stone"));

        config = builder.build();

        ConfigBuilder builder4 = new ConfigBuilder("test1", "test1-server", ConfigExtension.YAML, ConfigType.SERVER);

        builder4.push("mod_config");

        builder4.push("general");
        builder4.defineString("mod_name", "Test Mod");
        builder4.defineString("mod_version", "1.0.0");
        builder4.defineString("mod_author", "Test Author");
        builder4.pop();

        builder4.push("items");
        builder4.defineRegistry("item", Items.GRASS_BLOCK, BuiltInRegistries.ITEM);
        builder4.defineRegistry("block", Blocks.GRASS_BLOCK, BuiltInRegistries.BLOCK);
        builder4.defineString("custom_name", "Grass Block");
        builder4.defineInteger("max_stack_size", 64, 1, 64);
        builder4.pop();

        builder4.push("settings");
        builder4.defineInteger("integer_entry", 10, 0, 100);
        builder4.defineFloat("float_entry", 1.0F, 0.0F, 1.0F);
        builder4.defineDouble("double_entry", 1.0D, 0.0D, 1.0D);
        builder4.defineResourceLocation("resource_location_entry", ResourceLocation.fromNamespaceAndPath("minecraft", "stone"));
        builder4.defineEnum("enum_entry", Difficulty.NORMAL, Difficulty.class);
        builder4.defineDateTime("date_time_entry", LocalDateTime.of(2021, 1, 1, 0, 0, 0));
        builder4.pop();

        builder4.push("lists");
        builder4.defineStringList("string_list", List.of("test1", "test2", "test3"), 3, 10, "test\\d+");
        builder4.defineIntegerList("integer_list", List.of(1, 2, 3), 3, 10);
        builder4.defineFloatList("float_list", List.of(1.0F, 2.0F, 3.0F), 3, 10);
        builder4.defineDoubleList("double_list", List.of(1.0D, 2.0D, 3.0D), 3, 10);
        builder4.pop();
        builder4.pop();

        builder4.push("test");
        builder4.push("maps");
        builder4.defineStringMap("string_map", Map.of("test1", "1", "test2", "2", "test3", "3"), 3, 10);
        builder4.defineIntegerMap("integer_map", Map.of("test1", 1, "test2", 2, "test3", 3), 3, 10);
        builder4.defineFloatMap("float_map", Map.of("test1", 1.0F, "test2", 2.0F, "test3", 3.0F), 3, 10);
        builder4.defineDoubleMap("double_map", Map.of("test1", 1.0D, "test2", 2.0D, "test3", 3.0D), 3, 10);
        builder4.pop();
        builder4.pop();

        builder4.build();
    }
}
