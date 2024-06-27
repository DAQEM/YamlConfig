package com.daqem.yamlconfig.api.entry;

import com.daqem.yamlconfig.api.exception.ConfigEntryValidationException;
import net.minecraft.network.codec.StreamCodec;
import org.snakeyaml.engine.v2.nodes.Node;
import org.snakeyaml.engine.v2.nodes.NodeTuple;

import java.util.List;

public interface IConfigEntry<T> {

    String getKey();

    T getDefaultValue();

    T getValue();

    void setValue(T value);

    List<String> getComments();

    void setComments(List<String> comments);

    void addComment(String comment);

    IConfigEntry<T> withComments(List<String> comments);

    void validate(T value) throws ConfigEntryValidationException;

    <B extends IConfigEntry<T>>StreamCodec<B, NodeTuple> getCodec();

    default void encode(NodeTuple node) {
        getCodec().encode(this, node);
    }

    default NodeTuple decode() {
        return getCodec().decode(this);
    }
}
