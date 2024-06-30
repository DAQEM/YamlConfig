package com.daqem.yamlconfig.impl.config;

import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.api.config.ConfigExtension;
import com.daqem.yamlconfig.api.config.IConfig;
import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.config.entry.IStackConfigEntry;
import com.daqem.yamlconfig.yaml.YamlFileWriter;
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
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Config implements IConfig {

    private final String modId;
    private final String name;
    private final ConfigExtension extension;
    private final Path path;
    private final IStackConfigEntry context;
    private boolean isSynced = false;

    public Config(String modId, String name, ConfigExtension extension, Path path, IStackConfigEntry context) {
        this.modId = modId;
        this.name = name;
        this.extension = extension;
        this.path = path;
        this.context = context;
    }

    @Override
    public void load() {
        LoadSettings settings = LoadSettings.builder()
                .setParseComments(true)
                .build();
        try (FileInputStream inputStream = new FileInputStream(new File(path.toFile(), name + extension.getExtension()))) {
            Node node = new Compose(settings).composeInputStream(inputStream)
                    .orElseThrow(() -> new IOException("Failed to load config file: " + name + extension.getExtension()));
            if (node instanceof MappingNode mappingNode) {
                context.getType().getSerializer().encodeNode(context, new NodeTuple(new ScalarNode(Tag.STR, "parent", ScalarStyle.PLAIN), mappingNode));
            }
            this.isSynced = false;
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
        Dump dumper = new Dump(settings);
        try {
            dumper.dumpNode(context.getType().getSerializer().decodeNode(context).getValueNode(), new YamlFileWriter(this, StandardCharsets.UTF_8));
        } catch (FileNotFoundException e) {
            YamlConfig.LOGGER.error("Failed to save config file: " + name + "." + extension.getExtension(), e);
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
    public void sync(Map<String, ?> data) {
        if (data == null) return;
        for (Map.Entry<String, IConfigEntry<?>> entry : this.getSyncEntries().entrySet()) {
            IConfigEntry<?> configEntry = entry.getValue();
            if (data.containsKey(entry.getKey())) {
                //noinspection unchecked
                ((IConfigEntry<Object>) configEntry).setValue(data.get(entry.getKey()));
            }
        }
        isSynced = true;
    }

    @Override
    public boolean isSynced() {
        return isSynced;
    }

    @Override
    public void setSynced(boolean synced) {
        isSynced = synced;
    }
}
