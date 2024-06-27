package com.daqem.yamlconfig.impl;

import com.daqem.yamlconfig.YamlConfigExpectPlatform;
import com.daqem.yamlconfig.api.ConfigExtension;
import com.daqem.yamlconfig.api.IConfig;
import com.daqem.yamlconfig.api.IConfigBuilder;
import com.daqem.yamlconfig.api.entry.IConfigEntry;
import com.daqem.yamlconfig.api.entry.IStackConfigEntry;
import com.daqem.yamlconfig.api.exception.YamlConfigException;
import com.daqem.yamlconfig.impl.entry.StackConfigEntry;

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
    public void build() {
        if (isBuilt) {
            throw new IllegalStateException("Config has already been built");
        }
        IConfig config = new Config(this.modId, this.name, this.extension, this.path, contextStack.getFirst());
        config.load();
        config.save();

        this.isBuilt = true;
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
    public void pop() {
        if (contextStack.size() <= 1) {
            throw new IllegalStateException("Cannot pop the root context");
        }
        contextStack.pop();
    }

    @Override
    public void push(String key) {
        StackConfigEntry stackConfigEntry = new StackConfigEntry(key, new LinkedHashMap<>());
        contextStack.peek().getValue().put(key, stackConfigEntry);
        contextStack.push(stackConfigEntry);
    }
}
