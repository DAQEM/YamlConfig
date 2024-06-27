package com.daqem.yamlconfig.impl;

import com.daqem.yamlconfig.YamlConfigExpectPlatform;
import com.daqem.yamlconfig.api.ConfigExtension;
import com.daqem.yamlconfig.api.IConfig;
import com.daqem.yamlconfig.api.IConfigBuilder;
import com.daqem.yamlconfig.api.entry.*;
import com.daqem.yamlconfig.api.exception.YamlConfigException;
import com.daqem.yamlconfig.impl.entry.*;

import java.nio.file.Path;
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
        return config;
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
    public IIntegerListConfigEntry defineIntegerList(String key, List<Integer> defaultValue) {
        return define(new IntegerListConfigEntry(key, defaultValue));
    }

    @Override
    public IIntegerListConfigEntry defineIntegerList(String key, List<Integer> defaultValue, int minValue, int maxValue) {
        return define(new IntegerListConfigEntry(key, defaultValue, minValue, maxValue));
    }

    @Override
    public IIntegerListConfigEntry defineIntegerList(String key, List<Integer> defaultValue, int minValue, int maxValue, int minLength, int maxLength) {
        return define(new IntegerListConfigEntry(key, defaultValue, minValue, maxValue, minLength, maxLength));
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
