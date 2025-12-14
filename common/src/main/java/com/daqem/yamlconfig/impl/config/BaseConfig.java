package com.daqem.yamlconfig.impl.config;

import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.api.config.ConfigExtension;
import com.daqem.yamlconfig.api.config.ConfigType;
import com.daqem.yamlconfig.api.config.IConfig;
import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.config.entry.IStackConfigEntry;
import com.daqem.yamlconfig.api.config.entry.type.IConfigEntryType;
import com.daqem.yamlconfig.api.config.serializer.IConfigSerializer;
import com.daqem.yamlconfig.api.format.IConfigFormat;
import com.daqem.yamlconfig.api.node.IConfigNode;
import com.daqem.yamlconfig.api.node.IMapNode;
import com.daqem.yamlconfig.impl.config.entry.type.ConfigEntryTypes;
import com.daqem.yamlconfig.impl.node.ConfigMapNode;
import com.mojang.datafixers.util.Function5;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;

import java.io.*;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class BaseConfig implements IConfig {

    private final String modId;
    private final String name;
    private final ConfigExtension extension;
    private final ConfigType type;
    private final Path path;
    private final IStackConfigEntry context;
    private boolean isSynced = false;

    public BaseConfig(String modId, String name, ConfigExtension extension, ConfigType type, Path path, IStackConfigEntry context) {
        this.modId = modId;
        this.name = name;
        this.extension = extension;
        this.type = type;
        this.path = path;
        this.context = context;
    }

    @Override
    public void load() {
        IConfigFormat format = extension.createFormat();
        File file = new File(path.toFile(), name + extension.getExtension());

        if (!file.exists()) {
            YamlConfig.LOGGER.info("Config file not found, creating default: {}{}", name, extension.getExtension());
            save(); // Save defaults to create the file
            return;
        }

        try (FileReader reader = new FileReader(file)) {
            // 1. Read file into generic Node structure
            IMapNode rootNode = format.read(reader);

            // 2. Populate Config Entries from the Node structure
            deserializeFromNode(rootNode, context);

            setSynced(false);
            YamlConfig.LOGGER.info("Loaded config file: {}{}", name, extension.getExtension());
        } catch (Exception e) {
            YamlConfig.LOGGER.error("Failed to load config file: {}{}", name, extension.getExtension(), e);
        }
    }

    private void deserializeFromNode(IMapNode parent, IStackConfigEntry stack) {
        for (Map.Entry<String, IConfigEntry<?>> entrySet : stack.getEntries().entrySet()) {
            String key = entrySet.getKey();
            IConfigEntry<?> entry = entrySet.getValue();

            // If key is missing in file, we skip (keeping default value)
            if (!parent.containsKey(key)) continue;

            if (entry instanceof IStackConfigEntry childStack) {
                IConfigNode node = parent.get(key);
                if (node instanceof IMapNode mapNode) {
                    deserializeFromNode(mapNode, childStack);
                }
            } else {
                deserializeEntry(entry, parent);
            }
        }
    }

    private <T> void deserializeEntry(IConfigEntry<T> entry, IMapNode parent) {
        entry.getType().getSerializer().fromNode(entry, parent);
    }

    @Override
    public void save() {
        IConfigFormat format = extension.createFormat();
        File file = new File(path.toFile(), name + extension.getExtension());

        // Ensure directory exists
        if (file.getParentFile() != null) {
            if (!file.getParentFile().mkdirs() && !file.getParentFile().exists()) {
                YamlConfig.LOGGER.error("Failed to create config directory: {}", file.getParentFile().getAbsolutePath());
                return;
            }
        }

        try (FileWriter writer = new FileWriter(file)) {
            // 1. Convert Config Entries into generic Node structure
            ConfigMapNode rootNode = new ConfigMapNode();
            serializeToNode(rootNode, context);

            // 2. Write Node structure to file
            format.write(writer, rootNode);
            YamlConfig.LOGGER.info("Saved config file: {}{}", name, extension.getExtension());
        } catch (Exception e) {
            YamlConfig.LOGGER.error("Failed to save config file: {}{}", name, extension.getExtension(), e);
        }
    }

    private void serializeToNode(IMapNode parent, IStackConfigEntry stack) {
        for (IConfigEntry<?> entry : stack.get().values()) {
            if (entry instanceof IStackConfigEntry childStack) {
                ConfigMapNode childNode = new ConfigMapNode();
                // Transfer comments from the stack entry to the map node
                childNode.setComments(childStack.getComments().getComments());
                serializeToNode(childNode, childStack);
                parent.put(childStack.getKey(), childNode);
            } else {
                // Use helper to handle wildcard capture
                serializeEntry(entry, parent);
            }
        }
    }

    private <T> void serializeEntry(IConfigEntry<T> entry, IMapNode parent) {
        entry.getType().getSerializer().toNode(entry, parent);
    }

    @Override
    public void sync(Map<String, ?> data) {
        if (data == null) return;
        for (Map.Entry<String, IConfigEntry<?>> entry : this.getSyncEntries().entrySet()) {
            if (data.containsKey(entry.getKey())) {
                setEntryValue(entry.getValue(), data.get(entry.getKey()));
            }
        }
        setSynced(true);
    }

    @SuppressWarnings("unchecked")
    private <T> void setEntryValue(IConfigEntry<T> entry, Object value) {
        try {
            entry.set((T) value);
        } catch (ClassCastException e) {
            YamlConfig.LOGGER.error("Failed to sync config entry: {}", entry.getKey(), e);
        }
    }

    @Override
    public String getModId() {
        return modId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ConfigExtension getExtension() {
        return extension;
    }

    @Override
    public ConfigType getType() {
        return type;
    }

    @Override
    public Path getPath() {
        return path;
    }

    @Override
    public IStackConfigEntry getContext() {
        return context;
    }

    @Override
    public Map<String, IConfigEntry<?>> getEntries() {
        return context.getEntries();
    }

    @Override
    public Map<String, IConfigEntry<?>> getSyncEntries() {
        return context.getEntries().entrySet().stream()
                .filter(entry -> entry.getValue().shouldBeSynced())
                .collect(HashMap::new, (map, entry) -> map.put(entry.getKey(), entry.getValue()), Map::putAll);
    }

    @Override
    public boolean isSynced() {
        return isSynced;
    }

    @Override
    public void setSynced(boolean synced) {
        isSynced = synced;
    }

    @Override
    public Component getDisplayName() {
        return YamlConfig.translatable(modId + "." + name);
    }

    @Override
    public Component getModName() {
        return YamlConfig.translatable(modId);
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, BaseConfig> STREAM_CODEC = StreamCodec.of(
            (buf, config) -> {
                buf.writeEnum(config.getType());

                buf.writeUtf(config.getModId());
                buf.writeUtf(config.getName());
                buf.writeEnum(config.getExtension());
                buf.writeUtf(config.getPath().toString());
            },
            buf -> {
                ConfigType type = buf.readEnum(ConfigType.class);

                String modId = buf.readUtf();
                String name = buf.readUtf();
                ConfigExtension extension = buf.readEnum(ConfigExtension.class);
                Path path = Path.of(buf.readUtf());

                return switch (type) {
                    case CLIENT -> new ClientConfig(modId, name, extension, path, null);
                    case COMMON -> new CommonConfig(modId, name, extension, path, null);
                    case SERVER -> new ServerConfig(modId, name, extension, path, null);
                };
            }
    );

    @Override
    public void updateEntries(Map<String, IConfigEntry<?>> entries) {
        var existingEntries = context.getEntries();
        for (Map.Entry<String, IConfigEntry<?>> entry : entries.entrySet()) {
            if (existingEntries.containsKey(entry.getKey())) {
                IConfigEntry<?> existingEntry = existingEntries.get(entry.getKey());
                if (existingEntry.getType().equals(entry.getValue().getType())) {
                    //noinspection unchecked
                    ((IConfigEntry<Object>) existingEntry).set(entry.getValue().get());
                }
            }
        }
    }

    public static class BaseConfigSerializer<T extends IConfig> implements IConfigSerializer<T> {

        private final Function5<String, String, ConfigExtension, Path, IStackConfigEntry, T> configConstructor;

        public BaseConfigSerializer(Function5<String, String, ConfigExtension, Path, IStackConfigEntry, T> configConstructor) {
            this.configConstructor = configConstructor;
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, T config) {
            buf.writeUtf(config.getModId());
            buf.writeUtf(config.getName());
            buf.writeEnum(config.getExtension());
            buf.writeUtf(config.getPath().toString());

            IConfigEntryType<IStackConfigEntry, LinkedHashMap<String, IConfigEntry<?>>> type = ConfigEntryTypes.STACK;
            type.getSerializer().toNetwork(buf, config.getContext());
        }

        @Override
        public T fromNetwork(RegistryFriendlyByteBuf buf) {
            String modId = buf.readUtf();
            String name = buf.readUtf();
            ConfigExtension extension = buf.readEnum(ConfigExtension.class);
            Path path = Path.of(buf.readUtf());

            IConfigEntryType<IStackConfigEntry, LinkedHashMap<String, IConfigEntry<?>>> type = ConfigEntryTypes.STACK;
            IStackConfigEntry context = type.getSerializer().fromNetwork(buf);

            return configConstructor.apply(modId, name, extension, path, context);
        }
    }
}