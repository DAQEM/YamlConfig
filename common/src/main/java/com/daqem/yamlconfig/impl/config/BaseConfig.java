package com.daqem.yamlconfig.impl.config;

import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.api.config.ConfigExtension;
import com.daqem.yamlconfig.api.config.ConfigType;
import com.daqem.yamlconfig.api.config.IConfig;
import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.config.entry.IStackConfigEntry;
import com.daqem.yamlconfig.networking.s2c.ClientboundOpenConfigScreenPacket;
import com.daqem.yamlconfig.yaml.YamlFileWriter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import org.snakeyaml.engine.v2.api.Dump;
import org.snakeyaml.engine.v2.api.DumpSettings;
import org.snakeyaml.engine.v2.api.LoadSettings;
import org.snakeyaml.engine.v2.api.lowlevel.Compose;
import org.snakeyaml.engine.v2.common.FlowStyle;
import org.snakeyaml.engine.v2.common.ScalarStyle;
import org.snakeyaml.engine.v2.nodes.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
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
        LoadSettings settings = LoadSettings.builder()
                .setParseComments(true)
                .build();

        try (FileInputStream inputStream = new FileInputStream(new File(path.toFile(), name + extension.getExtension()))) {
            Compose compose = new Compose(settings);
            Node node = compose.composeInputStream(inputStream).orElseThrow();
            if (node instanceof MappingNode mappingNode) {
                ScalarNode keyNode = new ScalarNode(Tag.STR, "parent", ScalarStyle.PLAIN);
                NodeTuple nodeTuple = new NodeTuple(keyNode, mappingNode);
                context.getType().getSerializer().encodeNode(context, nodeTuple);
            }
            setSynced(false);
            YamlConfig.LOGGER.info("Loaded config file: " + name + extension.getExtension());
        } catch (IOException e) {
            if (e instanceof FileNotFoundException) {
                YamlConfig.LOGGER.info("Creating config file: " + name + extension.getExtension());
            } else {
                YamlConfig.LOGGER.error("Failed to load config file: " + name + extension.getExtension(), e);
            }
        }
    }

    @Override
    public void save() {
        DumpSettings settings = DumpSettings.builder()
                .setDefaultFlowStyle(FlowStyle.BLOCK)
                .setDumpComments(true)
                .build();

        try {
            Dump dumper = new Dump(settings);
            YamlFileWriter streamDataWriter = new YamlFileWriter(this);
            dumper.dumpNode(context.getType().getSerializer().decodeNode(context).getValueNode(), streamDataWriter);
        } catch (FileNotFoundException e) {
            YamlConfig.LOGGER.error("Failed to save config file: " + name + "." + extension.getExtension(), e);
        }
    }

    @Override
    public void sync(Map<String, ?> data) {
        if (data == null) return;
        for (Map.Entry<String, IConfigEntry<?>> entry : this.getSyncEntries().entrySet()) {
            IConfigEntry<?> configEntry = entry.getValue();
            if (data.containsKey(entry.getKey())) {
                //noinspection unchecked
                ((IConfigEntry<Object>) configEntry).setValue(data.get(entry.getKey()));
            }
        }
        setSynced(true);
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
}
