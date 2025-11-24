package com.daqem.yamlconfig.impl.format.toml;

import com.daqem.yamlconfig.api.config.ConfigExtension;
import com.daqem.yamlconfig.api.format.IConfigFormat;
import com.daqem.yamlconfig.api.node.*;
import com.daqem.yamlconfig.impl.node.*;
import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.toml.TomlParser;
import com.electronwill.nightconfig.toml.TomlWriter;

import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TomlFormat implements IConfigFormat {

    @Override
    public IMapNode read(Reader reader) {
        // NightConfig parses into a CommentedConfig object
        CommentedConfig tomlConfig = new TomlParser().parse(reader);
        return convertToGeneric(tomlConfig);
    }

    @Override
    public void write(Writer writer, IMapNode rootNode) {
        CommentedConfig tomlConfig = convertFromGeneric(rootNode);
        new TomlWriter().write(tomlConfig, writer);
    }

    @Override
    public String getExtension() {
        return ConfigExtension.TOML.getExtension();
    }

    private ConfigMapNode convertToGeneric(Config config) {
        ConfigMapNode mapNode = new ConfigMapNode();
        for (Config.Entry entry : config.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            IConfigNode node;

            if (value instanceof Config) {
                node = convertToGeneric((Config) value);
            } else if (value instanceof List) {
                node = convertListToGeneric((List<?>) value);
            } else {
                node = new ConfigValueNode<>(value);
            }

            // Extract comments
            if (config instanceof CommentedConfig commentedConfig) {
                String comment = commentedConfig.getComment(key);
                if (comment != null) {
                    node.setComments(Arrays.asList(comment.split("\n")));
                }
            }

            mapNode.put(key, node);
        }
        return mapNode;
    }

    private ConfigListNode convertListToGeneric(List<?> list) {
        ConfigListNode listNode = new ConfigListNode();
        for (Object obj : list) {
            if (obj instanceof Config) {
                listNode.add(convertToGeneric((Config) obj));
            } else if (obj instanceof List) {
                listNode.add(convertListToGeneric((List<?>) obj));
            } else {
                listNode.add(new ConfigValueNode<>(obj));
            }
        }
        return listNode;
    }

    private CommentedConfig convertFromGeneric(IMapNode node) {
        CommentedConfig config = CommentedConfig.inMemory();
        for (Map.Entry<String, IConfigNode> entry : node.entrySet()) {
            String key = entry.getKey();
            IConfigNode child = entry.getValue();
            Object value = resolveValue(child);

            config.set(key, value);

            if (child.getComments() != null && !child.getComments().isEmpty()) {
                config.setComment(key, String.join("\n", child.getComments()));
            }
        }
        return config;
    }

    private Object resolveValue(IConfigNode node) {
        if (node instanceof IMapNode mapNode) {
            // Recursion for nested maps
            return convertFromGeneric(mapNode);
        } else if (node instanceof IListNode listNode) {
            List<Object> rawList = new ArrayList<>();
            for (IConfigNode item : listNode.getValue()) {
                rawList.add(resolveValue(item));
            }
            return rawList;
        } else if (node instanceof IValueNode<?> valueNode) {
            return valueNode.getValue();
        }
        return null;
    }
}