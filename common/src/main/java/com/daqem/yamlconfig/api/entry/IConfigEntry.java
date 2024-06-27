package com.daqem.yamlconfig.api.entry;

import com.daqem.yamlconfig.api.IComments;
import com.daqem.yamlconfig.api.exception.ConfigEntryValidationException;
import com.daqem.yamlconfig.impl.Comments;
import net.minecraft.network.codec.StreamCodec;
import org.snakeyaml.engine.v2.comments.CommentLine;
import org.snakeyaml.engine.v2.comments.CommentType;
import org.snakeyaml.engine.v2.common.ScalarStyle;
import org.snakeyaml.engine.v2.nodes.NodeTuple;
import org.snakeyaml.engine.v2.nodes.ScalarNode;
import org.snakeyaml.engine.v2.nodes.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface IConfigEntry<T> {

    String getKey();

    T getDefaultValue();

    T getValue();

    void setValue(T value);

    IComments getComments();

    IConfigEntry<T> withComments(String... comments);

    IConfigEntry<T> withComments(boolean showDefaultValues, String... comments);

    IConfigEntry<T> withComments(boolean showDefaultValues, boolean showValidationParameters, String... comments);

    void validate(T value) throws ConfigEntryValidationException;

    <B extends IConfigEntry<T>>StreamCodec<B, NodeTuple> getCodec();

    default void encode(NodeTuple node) {
        getCodec().encode(this, node);
    }

    default NodeTuple decode() {
        return getCodec().decode(this);
    }

    default ScalarNode createKeyNode() {
        ScalarNode keyNode = new ScalarNode(Tag.STR, getKey(), ScalarStyle.PLAIN);
        keyNode.setBlockComments(getComments().getComments().stream()
                .map(c -> new CommentLine(Optional.empty(), Optional.empty(), c, CommentType.BLOCK))
                .toList());
        return keyNode;
    }
}
