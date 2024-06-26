package com.daqem.yamlconfig.impl;

import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.api.ConfigExtension;
import com.daqem.yamlconfig.api.IConfig;
import com.daqem.yamlconfig.api.entry.IStackConfigEntry;
import com.daqem.yamlconfig.yaml.YamlFileWriter;
import org.jetbrains.annotations.Nullable;
import org.snakeyaml.engine.v2.api.Dump;
import org.snakeyaml.engine.v2.api.DumpSettings;
import org.snakeyaml.engine.v2.api.LoadSettings;
import org.snakeyaml.engine.v2.api.lowlevel.Compose;
import org.snakeyaml.engine.v2.common.FlowStyle;
import org.snakeyaml.engine.v2.nodes.MappingNode;
import org.snakeyaml.engine.v2.nodes.Node;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class Config implements IConfig {

    private final String modId;
    private final String name;
    private final ConfigExtension extension;
    private final Path path;
    private final IStackConfigEntry context;
    private @Nullable MappingNode node;

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
                .setUseMarks(true)
                .build();
        try (FileInputStream inputStream = new FileInputStream(new File(path.toFile(), name + extension.getExtension()))) {
            Compose compose = new Compose(settings);
            Node node = compose.composeInputStream(inputStream)
                    .orElseThrow(() -> new IOException("Failed to load config file: " + name + extension.getExtension()));
            if (node instanceof MappingNode mappingNode) {
                this.node = mappingNode;
                context.applyValues(mappingNode);
            }
            YamlConfig.LOGGER.info("Loaded config file: " + name + extension.getExtension());
        } catch (IOException e) {
            YamlConfig.LOGGER.error("Failed to load config file: " + name + extension.getExtension(), e);
        }
    }

    @Override
    public void save() {
        DumpSettings settings = DumpSettings.builder()
                .setDefaultFlowStyle(FlowStyle.BLOCK)
                .setMultiLineFlow(true)
                .setDumpComments(true)
                .build();
        Dump dumper = new Dump(settings);
        try {
            dumper.dumpNode(node, new YamlFileWriter(this, StandardCharsets.UTF_8));
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
    public @Nullable MappingNode getNode() {
        return node;
    }
}
