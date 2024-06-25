package com.daqem.yamlconfig.impl;

import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.api.ConfigExtension;
import com.daqem.yamlconfig.api.IConfig;
import com.daqem.yamlconfig.api.entry.*;
import com.daqem.yamlconfig.api.exception.YamlConfigException;
import com.daqem.yamlconfig.impl.entry.StackConfigEntry;
import com.daqem.yamlconfig.yaml.YamlFileWriter;
import org.jetbrains.annotations.Nullable;
import org.snakeyaml.engine.v2.api.Dump;
import org.snakeyaml.engine.v2.api.DumpSettings;
import org.snakeyaml.engine.v2.api.LoadSettings;
import org.snakeyaml.engine.v2.api.lowlevel.Compose;
import org.snakeyaml.engine.v2.common.FlowStyle;
import org.snakeyaml.engine.v2.nodes.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class Config implements IConfig {

    private final String modId;
    private final String name;
    private final ConfigExtension extension;
    private final Path path;
    private final Map<String, IConfigEntry<?>> context;
    private @Nullable MappingNode node;

    public Config(String modId, String name, ConfigExtension extension, Path path, Map<String, IConfigEntry<?>> context) {
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
                applyValues(mappingNode, context);
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
    public Map<String, IConfigEntry<?>> getContext() {
        return context;
    }

    @Override
    public @Nullable MappingNode getNode() {
        return node;
    }

    @Override
    public Map<String, Object> toMap(Map<String, IConfigEntry<?>> context) {
        Map<String, Object> map = new LinkedHashMap<>();
        for (Map.Entry<String, IConfigEntry<?>> entry : context.entrySet()) {
            if (entry.getValue().getValue() instanceof Map<?, ?> mapValue) {
                //noinspection unchecked
                map.put(entry.getKey(), toMap((Map<String, IConfigEntry<?>>) mapValue));
            } else {
                map.put(entry.getKey(), entry.getValue().getValue());
            }
        }
        return map;
    }

    private void applyValues(MappingNode node, Map<String, IConfigEntry<?>> context) {
        for (Map.Entry<String, IConfigEntry<?>> entry : context.entrySet()) {
            IConfigEntry<?> configEntry = entry.getValue();
            List<NodeTuple> nodeList = node.getValue();
            Optional<Node> valueNode = nodeList.stream()
                    .filter(nodeTuple -> ((ScalarNode) nodeTuple.getKeyNode()).getValue().equals(entry.getKey()))
                    .map(NodeTuple::getValueNode)
                    .findFirst();
            if (valueNode.isPresent()) {
                if (valueNode.get() instanceof MappingNode mappingNode && configEntry instanceof StackConfigEntry stackEntry) {
                    applyValues(mappingNode, stackEntry.getValue());
                } else if (valueNode.get() instanceof ScalarNode scalarNode) {
                    String value = scalarNode.getValue();
                    if (configEntry instanceof IIntegerConfigEntry integerConfigEntry && scalarNode.getTag() == Tag.INT) {
                        integerConfigEntry.setValue(Integer.parseInt(value));
                    } else if (configEntry instanceof IStringConfigEntry stringConfigEntry && scalarNode.getTag() == Tag.STR) {
                        stringConfigEntry.setValue(value);
                    }
                } else if (valueNode.get() instanceof SequenceNode sequenceNode) {
                    List<ScalarNode> scalarNodes = sequenceNode.getValue().stream()
                            .filter(n -> n instanceof ScalarNode)
                            .map(n -> (ScalarNode) n)
                            .toList();

                    if (scalarNodes.size() == sequenceNode.getValue().size()) {
                        if (configEntry instanceof IIntegerListConfigEntry integerListConfigEntry) {
                            List<Integer> values = scalarNodes.stream()
                                    .map(n -> Integer.parseInt(n.getValue()))
                                    .toList();
                            integerListConfigEntry.setValue(values);
                        } else if (configEntry instanceof IStringListConfigEntry stringListConfigEntry) {
                            List<String> values = scalarNodes.stream()
                                    .map(ScalarNode::getValue)
                                    .toList();
                            stringListConfigEntry.setValue(values);
                        }
                    }
                }
            }
        }
    }
}
