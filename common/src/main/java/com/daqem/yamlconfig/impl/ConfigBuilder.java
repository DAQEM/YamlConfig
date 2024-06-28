package com.daqem.yamlconfig.impl;

import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.YamlConfigExpectPlatform;
import com.daqem.yamlconfig.api.ConfigExtension;
import com.daqem.yamlconfig.api.IConfig;
import com.daqem.yamlconfig.api.IConfigBuilder;
import com.daqem.yamlconfig.api.entry.*;
import com.daqem.yamlconfig.api.entry.list.IStringListConfigEntry;
import com.daqem.yamlconfig.api.entry.list.numeric.IDoubleListConfigEntry;
import com.daqem.yamlconfig.api.entry.list.numeric.IFloatListConfigEntry;
import com.daqem.yamlconfig.api.entry.list.numeric.IIntegerListConfigEntry;
import com.daqem.yamlconfig.api.entry.map.IStringMapConfigEntry;
import com.daqem.yamlconfig.api.entry.map.numeric.IDoubleMapConfigEntry;
import com.daqem.yamlconfig.api.entry.map.numeric.IFloatMapConfigEntry;
import com.daqem.yamlconfig.api.entry.map.numeric.IIntegerMapConfigEntry;
import com.daqem.yamlconfig.api.entry.minecraft.IRegistryConfigEntry;
import com.daqem.yamlconfig.api.entry.numeric.IDoubleConfigEntry;
import com.daqem.yamlconfig.api.entry.numeric.IFloatConfigEntry;
import com.daqem.yamlconfig.api.entry.numeric.IIntegerConfigEntry;
import com.daqem.yamlconfig.api.exception.YamlConfigException;
import com.daqem.yamlconfig.impl.entry.*;
import com.daqem.yamlconfig.impl.entry.list.StringListConfigEntry;
import com.daqem.yamlconfig.impl.entry.list.numeric.DoubleListConfigEntry;
import com.daqem.yamlconfig.impl.entry.list.numeric.FloatListConfigEntry;
import com.daqem.yamlconfig.impl.entry.list.numeric.IntegerListConfigEntry;
import com.daqem.yamlconfig.impl.entry.map.StringMapConfigEntry;
import com.daqem.yamlconfig.impl.entry.map.numeric.DoubleMapConfigEntry;
import com.daqem.yamlconfig.impl.entry.map.numeric.FloatMapConfigEntry;
import com.daqem.yamlconfig.impl.entry.map.numeric.IntegerMapConfigEntry;
import com.daqem.yamlconfig.impl.entry.minecraft.RegistryConfigEntry;
import com.daqem.yamlconfig.impl.entry.numeric.DoubleConfigEntry;
import com.daqem.yamlconfig.impl.entry.numeric.FloatConfigEntry;
import com.daqem.yamlconfig.impl.entry.numeric.IntegerConfigEntry;
import net.minecraft.core.Registry;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;

public class ConfigBuilder implements IConfigBuilder {

    private final String modId;
    private final String name;
    private final ConfigExtension extension;
    private final Path path;
    private boolean isBuilt = false;

    private final Deque<IStackConfigEntry> contextStack = new ArrayDeque<>(Collections.singletonList(new StackConfigEntry("parent", new LinkedHashMap<>())));

    public ConfigBuilder(String modId) {
        this(modId, modId);
    }

    public ConfigBuilder(String modId, String name) {
        this(modId, name, ConfigExtension.YAML);
    }

    public ConfigBuilder(String modId, String name, ConfigExtension extension) {
        this(modId, name, extension, YamlConfigExpectPlatform.getConfigDirectory());
    }

    public ConfigBuilder(String modId, String name, ConfigExtension extension, Path path) {
        this.modId = modId;
        this.name = name;
        this.extension = extension;
        this.path = path;
    }

    @Override
    public String getModId() {
        return this.modId;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public ConfigExtension getExtension() {
        return this.extension;
    }

    @Override
    public Path getPath() {
        return this.path;
    }

    @Override
    public boolean isBuilt() {
        return this.isBuilt;
    }

    @Override
    public IConfig build() {
        if (isBuilt) {
            throw new IllegalStateException("Config has already been built");
        }
        IConfig config = new Config(this.modId, this.name, this.extension, this.path, contextStack.getFirst());
        config.load();
        config.save();

        this.isBuilt = true;
        initializeConfig(config);
        return config;
    }

    private void initializeConfig(IConfig config) {
        YamlConfig.CONFIGS.computeIfAbsent(config.getModId(), k -> new HashMap<>()).put(config.getName(), config);
    }

    @Override
    public <T extends IConfigEntry<?>> T define(T entry) {
        if (isBuilt) {
            throw new YamlConfigException("Cannot define new entries after the config has been built");
        }
        contextStack.peek().getValue().put(entry.getKey(), entry);
        return entry;
    }

    @Override
    public IBooleanConfigEntry defineBoolean(String key, boolean defaultValue) {
        return define(new BooleanConfigEntry(key, defaultValue));
    }

    @Override
    public IIntegerConfigEntry defineInteger(String key, int defaultValue) {
        return define(new IntegerConfigEntry(key, defaultValue));
    }

    @Override
    public IIntegerConfigEntry defineInteger(String key, int defaultValue, int minValue, int maxValue) {
        return define(new IntegerConfigEntry(key, defaultValue, minValue, maxValue));
    }

    @Override
    public IFloatConfigEntry defineFloat(String key, float defaultValue) {
        return define(new FloatConfigEntry(key, defaultValue));
    }

    @Override
    public IFloatConfigEntry defineFloat(String key, float defaultValue, float minValue, float maxValue) {
        return define(new FloatConfigEntry(key, defaultValue, minValue, maxValue));
    }

    @Override
    public IDoubleConfigEntry defineDouble(String key, double defaultValue) {
        return define(new DoubleConfigEntry(key, defaultValue));
    }

    @Override
    public IDoubleConfigEntry defineDouble(String key, double defaultValue, double minValue, double maxValue) {
        return define(new DoubleConfigEntry(key, defaultValue, minValue, maxValue));
    }

    @Override
    public IIntegerListConfigEntry defineIntegerList(String key, List<Integer> defaultValue) {
        return define(new IntegerListConfigEntry(key, defaultValue));
    }

    @Override
    public IIntegerListConfigEntry defineIntegerList(String key, List<Integer> defaultValue, int minLength, int maxLength) {
        return define(new IntegerListConfigEntry(key, defaultValue, minLength, maxLength));
    }

    @Override
    public IIntegerListConfigEntry defineIntegerList(String key, List<Integer> defaultValue, int minLength, int maxLength, int minValue, int maxValue) {
        return define(new IntegerListConfigEntry(key, defaultValue, minLength, maxLength, minValue, maxValue));
    }

    @Override
    public IFloatListConfigEntry defineFloatList(String key, List<Float> defaultValue) {
        return define(new FloatListConfigEntry(key, defaultValue));
    }

    @Override
    public IFloatListConfigEntry defineFloatList(String key, List<Float> defaultValue, int minLength, int maxLength) {
        return define(new FloatListConfigEntry(key, defaultValue, minLength, maxLength));
    }

    @Override
    public IFloatListConfigEntry defineFloatList(String key, List<Float> defaultValue, int minLength, int maxLength, float minValue, float maxValue) {
        return define(new FloatListConfigEntry(key, defaultValue, minLength, maxLength, minValue, maxValue));
    }

    @Override
    public IDoubleListConfigEntry defineDoubleList(String key, List<Double> defaultValue) {
        return define(new DoubleListConfigEntry(key, defaultValue));
    }

    @Override
    public IDoubleListConfigEntry defineDoubleList(String key, List<Double> defaultValue, int minLength, int maxLength) {
        return define(new DoubleListConfigEntry(key, defaultValue, minLength, maxLength));
    }

    @Override
    public IDoubleListConfigEntry defineDoubleList(String key, List<Double> defaultValue, int minLength, int maxLength, double minValue, double maxValue) {
        return define(new DoubleListConfigEntry(key, defaultValue, minLength, maxLength, minValue, maxValue));
    }

    @Override
    public IIntegerMapConfigEntry defineIntegerMap(String key, Map<String, Integer> defaultValue) {
        return define(new IntegerMapConfigEntry(key, defaultValue));
    }

    @Override
    public IIntegerMapConfigEntry defineIntegerMap(String key, Map<String, Integer> defaultValue, int minLength, int maxLength) {
        return define(new IntegerMapConfigEntry(key, defaultValue, minLength, maxLength));
    }

    @Override
    public IIntegerMapConfigEntry defineIntegerMap(String key, Map<String, Integer> defaultValue, int minLength, int maxLength, int minValue, int maxValue) {
        return define(new IntegerMapConfigEntry(key, defaultValue, minLength, maxLength, minValue, maxValue));
    }

    @Override
    public IFloatMapConfigEntry defineFloatMap(String key, Map<String, Float> defaultValue) {
        return define(new FloatMapConfigEntry(key, defaultValue));
    }

    @Override
    public IFloatMapConfigEntry defineFloatMap(String key, Map<String, Float> defaultValue, int minLength, int maxLength) {
        return define(new FloatMapConfigEntry(key, defaultValue, minLength, maxLength));
    }

    @Override
    public IFloatMapConfigEntry defineFloatMap(String key, Map<String, Float> defaultValue, int minLength, int maxLength, float minValue, float maxValue) {
        return define(new FloatMapConfigEntry(key, defaultValue, minLength, maxLength, minValue, maxValue));
    }

    @Override
    public IDoubleMapConfigEntry defineDoubleMap(String key, Map<String, Double> defaultValue) {
        return define(new DoubleMapConfigEntry(key, defaultValue));
    }

    @Override
    public IDoubleMapConfigEntry defineDoubleMap(String key, Map<String, Double> defaultValue, int minLength, int maxLength) {
        return define(new DoubleMapConfigEntry(key, defaultValue, minLength, maxLength));
    }

    @Override
    public IDoubleMapConfigEntry defineDoubleMap(String key, Map<String, Double> defaultValue, int minLength, int maxLength, double minValue, double maxValue) {
        return define(new DoubleMapConfigEntry(key, defaultValue, minLength, maxLength, minValue, maxValue));
    }

    @Override
    public <E extends Enum<E>> IEnumConfigEntry<E> defineEnum(String key, E defaultValue, Class<E> enumClass) {
        return define(new EnumConfigEntry<>(key, defaultValue, enumClass));
    }

    @Override
    public IStringConfigEntry defineString(String key, String defaultValue) {
        return define(new StringConfigEntry(key, defaultValue));
    }

    @Override
    public IStringConfigEntry defineString(String key, String defaultValue, int minLength, int maxLength) {
        return define(new StringConfigEntry(key, defaultValue, minLength, maxLength));
    }

    @Override
    public IStringConfigEntry defineString(String key, String defaultValue, int minLength, int maxLength, String pattern) {
        return define(new StringConfigEntry(key, defaultValue, minLength, maxLength, pattern));
    }

    @Override
    public IStringConfigEntry defineString(String key, String defaultValue, int minLength, int maxLength, List<String> validValues) {
        return define(new StringConfigEntry(key, defaultValue, minLength, maxLength, validValues));
    }

    @Override
    public IStringConfigEntry defineString(String key, String defaultValue, int minLength, int maxLength, String pattern, List<String> validValues) {
        return define(new StringConfigEntry(key, defaultValue, minLength, maxLength, pattern, validValues));
    }

    @Override
    public IStringListConfigEntry defineStringList(String key, List<String> defaultValue) {
        return define(new StringListConfigEntry(key, defaultValue));
    }

    @Override
    public IStringListConfigEntry defineStringList(String key, List<String> defaultValue, int minLength, int maxLength) {
        return define(new StringListConfigEntry(key, defaultValue, minLength, maxLength));
    }

    @Override
    public IStringListConfigEntry defineStringList(String key, List<String> defaultValue, String pattern) {
        return define(new StringListConfigEntry(key, defaultValue, pattern));
    }

    @Override
    public IStringListConfigEntry defineStringList(String key, List<String> defaultValue, List<String> validValues) {
        return define(new StringListConfigEntry(key, defaultValue, validValues));
    }

    @Override
    public IStringListConfigEntry defineStringList(String key, List<String> defaultValue, String pattern, List<String> validValues) {
        return define(new StringListConfigEntry(key, defaultValue, pattern, validValues));
    }

    @Override
    public IStringListConfigEntry defineStringList(String key, List<String> defaultValue, int minLength, int maxLength, String pattern) {
        return define(new StringListConfigEntry(key, defaultValue, minLength, maxLength, pattern));
    }

    @Override
    public IStringListConfigEntry defineStringList(String key, List<String> defaultValue, int minLength, int maxLength, List<String> validValues) {
        return define(new StringListConfigEntry(key, defaultValue, minLength, maxLength, validValues));
    }

    @Override
    public IStringListConfigEntry defineStringList(String key, List<String> defaultValue, int minLength, int maxLength, String pattern, List<String> validValues) {
        return define(new StringListConfigEntry(key, defaultValue, minLength, maxLength, pattern, validValues));
    }

    @Override
    public IStringMapConfigEntry defineStringMap(String key, Map<String, String> defaultValue) {
        return define(new StringMapConfigEntry(key, defaultValue));
    }

    @Override
    public IStringMapConfigEntry defineStringMap(String key, Map<String, String> defaultValue, int minLength, int maxLength) {
        return define(new StringMapConfigEntry(key, defaultValue, minLength, maxLength));
    }

    @Override
    public IStringMapConfigEntry defineStringMap(String key, Map<String, String> defaultValue, int minLength, int maxLength, String pattern) {
        return define(new StringMapConfigEntry(key, defaultValue, minLength, maxLength, pattern));
    }

    @Override
    public IStringMapConfigEntry defineStringMap(String key, Map<String, String> defaultValue, int minLength, int maxLength, List<String> validValues) {
        return define(new StringMapConfigEntry(key, defaultValue, minLength, maxLength, validValues));
    }

    @Override
    public IStringMapConfigEntry defineStringMap(String key, Map<String, String> defaultValue, int minLength, int maxLength, String pattern, List<String> validValues) {
        return define(new StringMapConfigEntry(key, defaultValue, minLength, maxLength, pattern, validValues));
    }

    @Override
    public IDateTimeConfigEntry defineDateTime(String key, LocalDateTime defaultValue) {
        return define(new DateTimeConfigEntry(key, defaultValue));
    }

    @Override
    public IDateTimeConfigEntry defineDateTime(String key, LocalDateTime defaultValue, LocalDateTime minDateTime, LocalDateTime maxDateTime) {
        return define(new DateTimeConfigEntry(key, defaultValue, minDateTime, maxDateTime));
    }

    @Override
    public <T> IRegistryConfigEntry<T> defineRegistry(String key, T defaultValue, Registry<T> registry) {
        return define(new RegistryConfigEntry<>(key, defaultValue, registry));
    }

    @Override
    public void pop() {
        if (contextStack.size() <= 1) {
            throw new IllegalStateException("Cannot pop the root context");
        }
        contextStack.pop();
    }

    @Override
    public IStackConfigEntry push(String key) {
        StackConfigEntry stackConfigEntry = new StackConfigEntry(key, new LinkedHashMap<>());
        contextStack.peek().getValue().put(key, stackConfigEntry);
        contextStack.push(stackConfigEntry);
        return stackConfigEntry;
    }
}
