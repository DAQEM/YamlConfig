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

public class TestConfig {

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
        ConfigBuilder builder = new ConfigBuilder("test", "test-common", ConfigExtension.YAML, ConfigType.COMMON);

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

//        ConfigBuilder builder1 = new ConfigBuilder("test", "test-server", ConfigExtension.YAML, ConfigType.SERVER);
//        builder1.build();
//
//        ConfigBuilder builder2 = new ConfigBuilder("test", "test-client", ConfigExtension.YAML, ConfigType.CLIENT);
//        builder2.build();
//
//        ConfigBuilder builder3 = new ConfigBuilder("test1", "test1-client", ConfigExtension.YAML, ConfigType.CLIENT);
//        builder3.build();

        ConfigBuilder builder4 = new ConfigBuilder("test1", "test1-common", ConfigExtension.YAML, ConfigType.COMMON);

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
        builder4.push("test1");
        builder4.push("test2");
        builder4.push("test3");
        builder4.push("test4");
        builder4.push("test5");
        builder4.push("test6");
        builder4.push("test7");
        builder4.push("test8");
        builder4.push("test9");
        builder4.push("test10");
        builder4.push("test11");
        builder4.push("test12");
        builder4.push("test13");
        builder4.push("test14");
        builder4.push("test15");
        builder4.push("test16");
        builder4.push("test17");
        builder4.push("test18");
        builder4.push("test19");
        builder4.push("test20");
        builder4.push("test21");
        builder4.push("test22");
        builder4.push("test23");
        builder4.push("test24");
        builder4.push("test25");
        builder4.push("test26");
        builder4.push("test27");
        builder4.push("test28");
        builder4.push("test29");
        builder4.push("test30");

        builder4.push("maps");
        builder4.defineStringMap("string_map", Map.of("test1", "1", "test2", "2", "test3", "3"), 3, 10);
        builder4.defineIntegerMap("integer_map", Map.of("test1", 1, "test2", 2, "test3", 3), 3, 10);
        builder4.defineFloatMap("float_map", Map.of("test1", 1.0F, "test2", 2.0F, "test3", 3.0F), 3, 10);
        builder4.defineDoubleMap("double_map", Map.of("test1", 1.0D, "test2", 2.0D, "test3", 3.0D), 3, 10);
        builder4.pop();

        builder4.pop();
        builder4.pop();
        builder4.pop();
        builder4.pop();
        builder4.pop();
        builder4.pop();
        builder4.pop();
        builder4.pop();
        builder4.pop();
        builder4.pop();
        builder4.pop();
        builder4.pop();
        builder4.pop();
        builder4.pop();
        builder4.pop();
        builder4.pop();
        builder4.pop();
        builder4.pop();
        builder4.pop();
        builder4.pop();
        builder4.pop();
        builder4.pop();
        builder4.pop();
        builder4.pop();
        builder4.pop();
        builder4.pop();
        builder4.pop();
        builder4.pop();
        builder4.pop();
        builder4.pop();
        builder4.pop();

        builder4.build();

//        ConfigBuilder builder5 = new ConfigBuilder("test2", "test2-client", ConfigExtension.YAML, ConfigType.CLIENT);
//        builder5.build();
//
//        ConfigBuilder builder6 = new ConfigBuilder("test2", "test2-common", ConfigExtension.YAML, ConfigType.COMMON);
//        builder6.build();
//
//        ConfigBuilder builder7 = new ConfigBuilder("test2", "test2-server", ConfigExtension.YAML, ConfigType.SERVER);
//        builder7.build();
//
//        ConfigBuilder builder8 = new ConfigBuilder("test3", "test3-client", ConfigExtension.YAML, ConfigType.CLIENT);
//        builder8.build();
//
//        ConfigBuilder builder9 = new ConfigBuilder("test3", "test3-common", ConfigExtension.YAML, ConfigType.COMMON);
//        builder9.build();
//
//        ConfigBuilder builder10 = new ConfigBuilder("test3", "test3-server", ConfigExtension.YAML, ConfigType.SERVER);
//        builder10.build();
//
//        ConfigBuilder builder11 = new ConfigBuilder("test4", "test4-client", ConfigExtension.YAML, ConfigType.CLIENT);
//        builder11.build();
//
//        ConfigBuilder builder12 = new ConfigBuilder("test4", "test4-common", ConfigExtension.YAML, ConfigType.COMMON);
//        builder12.build();
//
//        ConfigBuilder builder13 = new ConfigBuilder("test4", "test4-server", ConfigExtension.YAML, ConfigType.SERVER);
//        builder13.build();
//
//        ConfigBuilder builder14 = new ConfigBuilder("test5", "test5-client", ConfigExtension.YAML, ConfigType.CLIENT);
//        builder14.build();
    }
}
