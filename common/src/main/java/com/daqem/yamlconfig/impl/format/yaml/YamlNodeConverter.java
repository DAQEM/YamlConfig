package com.daqem.yamlconfig.impl.format.yaml;

import com.daqem.yamlconfig.api.node.*;
import com.daqem.yamlconfig.impl.node.*;
import org.snakeyaml.engine.v2.common.FlowStyle;
import org.snakeyaml.engine.v2.common.ScalarStyle;
import org.snakeyaml.engine.v2.nodes.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class YamlNodeConverter {

    public static IMapNode toGeneric(Node root) {
        if (root instanceof MappingNode mappingNode) {
            return (IMapNode) convertNodeToGeneric(mappingNode);
        }
        return new ConfigMapNode();
    }

    private static IConfigNode convertNodeToGeneric(Node node) {
        IConfigNode result;
        if (node instanceof ScalarNode scalar) {
            // Attempt to parse primitive types, fallback to string
            result = new ConfigValueNode<>(parseScalar(scalar.getValue()));
        } else if (node instanceof SequenceNode sequence) {
            ConfigListNode listNode = new ConfigListNode();
            for (Node child : sequence.getValue()) {
                listNode.add(convertNodeToGeneric(child));
            }
            result = listNode;
        } else if (node instanceof MappingNode mapping) {
            ConfigMapNode mapNode = new ConfigMapNode();
            for (NodeTuple tuple : mapping.getValue()) {
                if (tuple.getKeyNode() instanceof ScalarNode keyNode) {
                    mapNode.put(keyNode.getValue(), convertNodeToGeneric(tuple.getValueNode()));
                }
            }
            result = mapNode;
        } else {
            result = new ConfigValueNode<>(null);
        }

        // Extract Block Comments
        if (node.getBlockComments() != null && !node.getBlockComments().isEmpty()) {
            List<String> comments = new ArrayList<>();
            for (org.snakeyaml.engine.v2.comments.CommentLine line : node.getBlockComments()) {
                comments.add(line.getValue());
            }
            result.setComments(comments);
        }
        return result;
    }

    private static Object parseScalar(String value) {
        if ("true".equalsIgnoreCase(value)) return true;
        if ("false".equalsIgnoreCase(value)) return false;
        try { return Integer.parseInt(value); } catch (NumberFormatException ignored) {}
        try { return Double.parseDouble(value); } catch (NumberFormatException ignored) {}
        return value;
    }

    public static Node fromGeneric(IMapNode root) {
        return convertGenericToNode(root);
    }

    private static Node convertGenericToNode(IConfigNode node) {
        Node result;
        if (node instanceof IValueNode<?> valueNode) {
            Object val = valueNode.getValue();
            String strVal = val == null ? "" : val.toString();
            // Determine scalar style/tag based on type
            Tag tag = Tag.STR;
            if (val instanceof Integer) tag = Tag.INT;
            else if (val instanceof Double || val instanceof Float) tag = Tag.FLOAT;
            else if (val instanceof Boolean) tag = Tag.BOOL;

            result = new ScalarNode(tag, strVal, ScalarStyle.PLAIN);

        } else if (node instanceof IListNode listNode) {
            List<Node> children = new ArrayList<>();
            for (IConfigNode child : listNode.getValue()) {
                children.add(convertGenericToNode(child));
            }
            result = new SequenceNode(Tag.SEQ, children, FlowStyle.BLOCK);

        } else if (node instanceof IMapNode mapNode) {
            List<NodeTuple> tuples = new ArrayList<>();
            for (var entry : mapNode.entrySet()) {
                Node key = new ScalarNode(Tag.STR, entry.getKey(), ScalarStyle.PLAIN);
                Node value = convertGenericToNode(entry.getValue());
                tuples.add(new NodeTuple(key, value));
            }
            result = new MappingNode(Tag.MAP, tuples, FlowStyle.BLOCK);
        } else {
            throw new IllegalArgumentException("Unknown node type: " + node.getClass());
        }

        // Apply Comments
        if (node.getComments() != null && !node.getComments().isEmpty()) {
            List<org.snakeyaml.engine.v2.comments.CommentLine> commentLines = new ArrayList<>();
            for (String comment : node.getComments()) {
                commentLines.add(new org.snakeyaml.engine.v2.comments.CommentLine(
                        Optional.empty(), Optional.empty(), comment,
                        org.snakeyaml.engine.v2.comments.CommentType.BLOCK
                ));
            }
            result.setBlockComments(commentLines);
        }

        return result;
    }
}