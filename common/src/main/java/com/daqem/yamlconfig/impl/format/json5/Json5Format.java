package com.daqem.yamlconfig.impl.format.json5;

import blue.endless.jankson.*;
import com.daqem.yamlconfig.api.config.ConfigExtension;
import com.daqem.yamlconfig.api.format.IConfigFormat;
import com.daqem.yamlconfig.api.node.*;
import com.daqem.yamlconfig.impl.node.*;
import com.google.common.io.CharStreams;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Arrays;
import java.util.Map;

public class Json5Format implements IConfigFormat {

    private final Jankson jankson = Jankson.builder().build();

    @Override
    public IMapNode read(Reader reader) throws IOException {
        try {
            JsonObject jsonObject = jankson.load(CharStreams.toString(reader));
            return (IMapNode) convertToGeneric(jsonObject);
        } catch (Exception e) {
            throw new IOException("Failed to parse JSON5", e);
        }
    }

    @Override
    public void write(Writer writer, IMapNode rootNode) throws IOException {
        JsonElement jsonElement = convertFromGeneric(rootNode);
        // Jankson doesn't write directly to a writer easily, we format to string first
        writer.write(jsonElement.toJson(true, true));
    }

    @Override
    public String getExtension() {
        return ConfigExtension.JSON5.getExtension();
    }

    private IConfigNode convertToGeneric(JsonElement element) {
        if (element instanceof JsonObject jsonObject) {
            ConfigMapNode mapNode = new ConfigMapNode();
            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                IConfigNode child = convertToGeneric(entry.getValue());

                String comment = jsonObject.getComment(entry.getKey());
                if (comment != null) {
                    child.setComments(Arrays.asList(comment.split("\n")));
                }

                mapNode.put(entry.getKey(), child);
            }
            return mapNode;
        } else if (element instanceof JsonArray jsonArray) {
            ConfigListNode listNode = new ConfigListNode();
            for (JsonElement child : jsonArray) {
                listNode.add(convertToGeneric(child));
            }
            return listNode;
        } else if (element instanceof JsonPrimitive primitive) {
            return new ConfigValueNode<>(primitive.getValue());
        } else if (element instanceof JsonNull) {
            return new ConfigValueNode<>(null);
        }
        throw new IllegalArgumentException("Unknown JSON Element: " + element.getClass());
    }

    private JsonElement convertFromGeneric(IConfigNode node) {
        if (node instanceof IMapNode mapNode) {
            JsonObject jsonObject = new JsonObject();
            for (Map.Entry<String, IConfigNode> entry : mapNode.entrySet()) {
                JsonElement child = convertFromGeneric(entry.getValue());
                jsonObject.put(entry.getKey(), child);

                if (entry.getValue().getComments() != null && !entry.getValue().getComments().isEmpty()) {
                    jsonObject.setComment(entry.getKey(), String.join("\n", entry.getValue().getComments()));
                }
            }
            return jsonObject;
        } else if (node instanceof IListNode listNode) {
            JsonArray jsonArray = new JsonArray();
            for (IConfigNode child : listNode.getValue()) {
                jsonArray.add(convertFromGeneric(child));
            }
            return jsonArray;
        } else if (node instanceof IValueNode<?> valueNode) {
            Object val = valueNode.getValue();
            if (val == null) return JsonNull.INSTANCE;
            return new JsonPrimitive(val);
        }
        return JsonNull.INSTANCE;
    }
}