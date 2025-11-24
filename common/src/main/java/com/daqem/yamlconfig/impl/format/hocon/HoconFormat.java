package com.daqem.yamlconfig.impl.format.hocon;

import com.daqem.yamlconfig.api.config.ConfigExtension;
import com.daqem.yamlconfig.api.format.IConfigFormat;
import com.daqem.yamlconfig.api.node.*;
import com.daqem.yamlconfig.impl.node.*;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.loader.HeaderMode;

import java.io.*;
import java.util.Arrays;
import java.util.Map;

public class HoconFormat implements IConfigFormat {

    @Override
    public IMapNode read(Reader reader) throws IOException {
        // Configurate requires a BufferedReader
        BufferedReader bufferedReader = (reader instanceof BufferedReader) ? (BufferedReader) reader : new BufferedReader(reader);

        HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
                .source(() -> bufferedReader)
                .build();

        try {
            CommentedConfigurationNode root = loader.load();
            return (IMapNode) convertToGeneric(root);
        } catch (ConfigurateException e) {
            throw new IOException("Failed to load HOCON", e);
        }
    }

    @Override
    public void write(Writer writer, IMapNode rootNode) throws IOException {
        // Configurate requires a BufferedWriter
        BufferedWriter bufferedWriter = (writer instanceof BufferedWriter) ? (BufferedWriter) writer : new BufferedWriter(writer);

        HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
                .sink(() -> bufferedWriter)
                .headerMode(HeaderMode.PRESERVE)
                .build();

        try {
            CommentedConfigurationNode root = loader.createNode();
            convertFromGeneric(root, rootNode);
            loader.save(root);
        } catch (ConfigurateException e) {
            throw new IOException("Failed to save HOCON", e);
        }
    }

    @Override
    public String getExtension() {
        return ConfigExtension.HOCON.getExtension();
    }

    private IConfigNode convertToGeneric(ConfigurationNode hoconNode) {
        if (hoconNode.isMap()) {
            ConfigMapNode mapNode = new ConfigMapNode();
            for (Map.Entry<Object, ? extends ConfigurationNode> entry : hoconNode.childrenMap().entrySet()) {
                IConfigNode child = convertToGeneric(entry.getValue());
                mapNode.put(entry.getKey().toString(), child);
            }
            extractComment(hoconNode, mapNode);
            return mapNode;
        } else if (hoconNode.isList()) {
            ConfigListNode listNode = new ConfigListNode();
            for (ConfigurationNode child : hoconNode.childrenList()) {
                listNode.add(convertToGeneric(child));
            }
            extractComment(hoconNode, listNode);
            return listNode;
        } else {
            ConfigValueNode<Object> valueNode = new ConfigValueNode<>(hoconNode.raw());
            extractComment(hoconNode, valueNode);
            return valueNode;
        }
    }

    private void extractComment(ConfigurationNode hoconNode, IConfigNode myNode) {
        if (hoconNode instanceof CommentedConfigurationNode commented) {
            String comment = commented.comment();
            if (comment != null) {
                myNode.setComments(Arrays.asList(comment.split("\n")));
            }
        }
    }

    private void convertFromGeneric(CommentedConfigurationNode hoconNode, IConfigNode myNode) {
        if (myNode.getComments() != null && !myNode.getComments().isEmpty()) {
            hoconNode.comment(String.join("\n", myNode.getComments()));
        }

        if (myNode instanceof IMapNode mapNode) {
            for (Map.Entry<String, IConfigNode> entry : mapNode.entrySet()) {
                convertFromGeneric(hoconNode.node(entry.getKey()), entry.getValue());
            }
        } else if (myNode instanceof IListNode listNode) {
            // HOCON lists need appending
            for (IConfigNode child : listNode.getValue()) {
                convertFromGeneric(hoconNode.appendListNode(), child);
            }
        } else if (myNode instanceof IValueNode<?> valueNode) {
            try {
                hoconNode.set(valueNode.getValue());
            } catch (ConfigurateException e) {
                // Should technically not happen with simple primitives
                throw new RuntimeException(e);
            }
        }
    }
}