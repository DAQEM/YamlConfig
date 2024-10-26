package com.daqem.yamlconfig.impl.config.entry.type;

import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.api.config.entry.*;
import com.daqem.yamlconfig.api.config.entry.list.IStringListConfigEntry;
import com.daqem.yamlconfig.api.config.entry.list.numeric.IDoubleListConfigEntry;
import com.daqem.yamlconfig.api.config.entry.list.numeric.IFloatListConfigEntry;
import com.daqem.yamlconfig.api.config.entry.list.numeric.IIntegerListConfigEntry;
import com.daqem.yamlconfig.api.config.entry.map.IStringMapConfigEntry;
import com.daqem.yamlconfig.api.config.entry.map.numeric.IDoubleMapConfigEntry;
import com.daqem.yamlconfig.api.config.entry.map.numeric.IFloatMapConfigEntry;
import com.daqem.yamlconfig.api.config.entry.map.numeric.IIntegerMapConfigEntry;
import com.daqem.yamlconfig.api.config.entry.minecraft.IRegistryConfigEntry;
import com.daqem.yamlconfig.api.config.entry.minecraft.IResourceLocationConfigEntry;
import com.daqem.yamlconfig.api.config.entry.numeric.IDoubleConfigEntry;
import com.daqem.yamlconfig.api.config.entry.numeric.IFloatConfigEntry;
import com.daqem.yamlconfig.api.config.entry.numeric.IIntegerConfigEntry;
import com.daqem.yamlconfig.api.config.entry.serializer.IConfigEntrySerializer;
import com.daqem.yamlconfig.api.config.entry.type.IConfigEntryType;
import com.daqem.yamlconfig.impl.config.entry.*;
import com.daqem.yamlconfig.impl.config.entry.list.numeric.IntegerListConfigEntry;
import com.daqem.yamlconfig.impl.config.entry.map.numeric.DoubleMapConfigEntry;
import com.daqem.yamlconfig.impl.config.entry.numeric.IntegerConfigEntry;
import com.daqem.yamlconfig.impl.config.entry.list.StringListConfigEntry;
import com.daqem.yamlconfig.impl.config.entry.list.numeric.DoubleListConfigEntry;
import com.daqem.yamlconfig.impl.config.entry.list.numeric.FloatListConfigEntry;
import com.daqem.yamlconfig.impl.config.entry.map.StringMapConfigEntry;
import com.daqem.yamlconfig.impl.config.entry.map.numeric.FloatMapConfigEntry;
import com.daqem.yamlconfig.impl.config.entry.map.numeric.IntegerMapConfigEntry;
import com.daqem.yamlconfig.impl.config.entry.minecraft.RegistryConfigEntry;
import com.daqem.yamlconfig.impl.config.entry.minecraft.ResourceLocationConfigEntry;
import com.daqem.yamlconfig.impl.config.entry.numeric.DoubleConfigEntry;
import com.daqem.yamlconfig.impl.config.entry.numeric.FloatConfigEntry;
import com.daqem.yamlconfig.registry.YamlConfigRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public abstract class ConfigEntryTypes {

    public static final IConfigEntryType<IStringConfigEntry, String> STRING = register(YamlConfig.getId("string"), new StringConfigEntry.Serializer());
    public static final IConfigEntryType<IStackConfigEntry, Map<String, IConfigEntry<?>>> STACK = register(YamlConfig.getId("stack"), new StackConfigEntry.Serializer());
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static final IConfigEntryType<IEnumConfigEntry<? extends Enum<?>>, ? extends Enum<?>> ENUM = register(YamlConfig.getId("enum"), new EnumConfigEntry.Serializer());
    public static final IConfigEntryType<IDateTimeConfigEntry, LocalDateTime> DATE_TIME = register(YamlConfig.getId("datetime"), new DateTimeConfigEntry.Serializer());
    public static final IConfigEntryType<IBooleanConfigEntry, Boolean> BOOLEAN = register(YamlConfig.getId("boolean"), new BooleanConfigEntry.Serializer());
    public static final IConfigEntryType<IIntegerConfigEntry, Integer> INTEGER = register(YamlConfig.getId("integer"), new IntegerConfigEntry.Serializer());
    public static final IConfigEntryType<IFloatConfigEntry, Float> FLOAT = register(YamlConfig.getId("float"), new FloatConfigEntry.Serializer());
    public static final IConfigEntryType<IDoubleConfigEntry, Double> DOUBLE = register(YamlConfig.getId("double"), new DoubleConfigEntry.Serializer());
    public static final IConfigEntryType<IResourceLocationConfigEntry, ResourceLocation> RESOURCE_LOCATION = register(YamlConfig.getId("resource_location"), new ResourceLocationConfigEntry.Serializer());
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static final IConfigEntryType<IRegistryConfigEntry<?>, ?> REGISTRY = register(YamlConfig.getId("registry"), new RegistryConfigEntry.Serializer());
    public static final IConfigEntryType<IStringListConfigEntry, List<String>> STRING_LIST = register(YamlConfig.getId("string_list"), new StringListConfigEntry.Serializer());
    public static final IConfigEntryType<IIntegerListConfigEntry, List<Integer>> INTEGER_LIST = register(YamlConfig.getId("integer_list"), new IntegerListConfigEntry.Serializer());
    public static final IConfigEntryType<IFloatListConfigEntry, List<Float>> FLOAT_LIST = register(YamlConfig.getId("float_list"), new FloatListConfigEntry.Serializer());
    public static final IConfigEntryType<IDoubleListConfigEntry, List<Double>> DOUBLE_LIST = register(YamlConfig.getId("double_list"), new DoubleListConfigEntry.Serializer());
    public static final IConfigEntryType<IStringMapConfigEntry, Map<String, String>> STRING_MAP = register(YamlConfig.getId("string_map"), new StringMapConfigEntry.Serializer());
    public static final IConfigEntryType<IIntegerMapConfigEntry, Map<String, Integer>> INTEGER_MAP = register(YamlConfig.getId("integer_map"), new IntegerMapConfigEntry.Serializer());
    public static final IConfigEntryType<IFloatMapConfigEntry, Map<String, Float>> FLOAT_MAP = register(YamlConfig.getId("float_map"), new FloatMapConfigEntry.Serializer());
    public static final IConfigEntryType<IDoubleMapConfigEntry, Map<String, Double>> DOUBLE_MAP = register(YamlConfig.getId("double_map"), new DoubleMapConfigEntry.Serializer());


    static <C extends IConfigEntry<T>, T> IConfigEntryType<C, T> register(ResourceLocation id, IConfigEntrySerializer<C, T> serializer) {
        return Registry.register(YamlConfigRegistry.CONFIG_ENTRY, id, new IConfigEntryType<>() {
            @Override
            public ResourceLocation getId() {
                return id;
            }

            @Override
            public IConfigEntrySerializer<C, T> getSerializer() {
                return serializer;
            }
        });
    }

    public static void init() {
    }
}
