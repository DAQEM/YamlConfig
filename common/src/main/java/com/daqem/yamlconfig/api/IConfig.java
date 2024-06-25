package com.daqem.yamlconfig.api;

import com.daqem.yamlconfig.api.entry.IConfigEntry;
import org.snakeyaml.engine.v2.nodes.MappingNode;
import org.snakeyaml.engine.v2.nodes.Node;

import java.nio.file.Path;
import java.util.Map;

public interface IConfig {

    void load();

    void save();

    String getModId();

    String getName();

    ConfigExtension getExtension();

    Path getPath();

    Map<String, IConfigEntry<?>> getContext();

    MappingNode getNode();

    Map<String, Object> toMap(Map<String, IConfigEntry<?>> context);
}
