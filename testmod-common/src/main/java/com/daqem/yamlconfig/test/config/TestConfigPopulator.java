package com.daqem.yamlconfig.test.config;

import com.daqem.yamlconfig.api.config.IConfigBuilder;
import com.daqem.yamlconfig.test.config.entry.TestConfigEntry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Difficulty;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class TestConfigPopulator {

    public static void populate(IConfigBuilder builder) {
        // 1. Primitives
        builder.push("primitives")
                .withComments("Basic primitive types");

        builder.defineBoolean("boolean_val", true)
                .withComments("A boolean value");

        builder.defineInteger("integer_val", 10)
                .withComments("An integer value");
        builder.defineInteger("integer_bounded", 50, 0, 100)
                .withComments("An integer between 0 and 100");

        builder.defineLong("long_val", 1000000L)
                .withComments("A long value");
        builder.defineLong("long_bounded", 500L, 0L, 1000L)
                .withComments("A long between 0 and 1000");

        builder.defineFloat("float_val", 1.5F)
                .withComments("A float value");
        builder.defineFloat("float_bounded", 0.5F, 0.0F, 1.0F)
                .withComments("A float between 0.0 and 1.0");

        builder.defineDouble("double_val", 1.5D)
                .withComments("A double value");
        builder.defineDouble("double_bounded", 0.5D, 0.0D, 1.0D)
                .withComments("A double between 0.0 and 1.0");

        builder.defineString("string_val", "Hello World")
                .withComments("A simple string");
        builder.defineString("string_pattern", "test_123", 0, 20, "^[a-z_0-9]+$")
                .withComments("A string matching regex ^[a-z_0-9]+$");
        builder.defineString("string_selection", "A", 0, 10, List.of("A", "B", "C"))
                .withComments("A string selected from [A, B, C]");

        builder.pop(); // End Primitives

        // 2. Lists
        builder.push("lists")
                .withComments("List types");

        builder.defineStringList("string_list", List.of("one", "two", "three"))
                .withComments("A list of strings");
        builder.defineStringList("string_list_pattern", List.of("a1", "b2"), "^[a-z][0-9]$")
                .withComments("A list of strings matching ^[a-z][0-9]$");

        builder.defineIntegerList("integer_list", List.of(1, 2, 3, 4, 5));
        builder.defineIntegerList("integer_list_bounded", List.of(5, 10), 0, 5, 0, 20)
                .withComments("Max 5 items, values between 0-20");

        builder.defineFloatList("float_list", List.of(1.1F, 2.2F));
        builder.defineDoubleList("double_list", List.of(1.1D, 2.2D));

        builder.pop(); // End Lists

        // 3. Maps
        builder.push("maps")
                .withComments("Map types");

        builder.defineStringMap("string_map", Map.of("key1", "val1", "key2", "val2"));
        builder.defineIntegerMap("integer_map", Map.of("first", 1, "second", 2));
        builder.defineFloatMap("float_map", Map.of("a", 1.5F, "b", 2.5F));
        builder.defineDoubleMap("double_map", Map.of("x", 10.5D, "y", 20.5D));

        builder.pop(); // End Maps

        // 4. Minecraft / Advanced
        builder.push("minecraft")
                .withComments("Minecraft specific types and Enums");

        builder.defineResourceLocation("resource_location", ResourceLocation.fromNamespaceAndPath("minecraft", "dirt"));
        builder.defineRegistry("item_registry", Items.DIAMOND_SWORD, BuiltInRegistries.ITEM);
        builder.defineRegistry("block_registry", Blocks.STONE, BuiltInRegistries.BLOCK);
        builder.defineEnum("difficulty_enum", Difficulty.HARD, Difficulty.class);

        builder.pop(); // End Minecraft

        // 5. Special / Custom
        builder.push("special");

        builder.defineDateTime("local_date_time", LocalDateTime.now());
        builder.define(new TestConfigEntry("custom_entry", "Custom Serializer Value"));

        builder.pop(); // End Special
    }
}