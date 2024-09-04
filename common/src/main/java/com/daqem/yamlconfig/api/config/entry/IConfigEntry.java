package com.daqem.yamlconfig.api.config.entry;

import com.daqem.yamlconfig.api.config.entry.comment.IComments;
import com.daqem.yamlconfig.api.config.entry.type.IConfigEntryType;
import com.daqem.yamlconfig.api.exception.ConfigEntryValidationException;
import com.daqem.yamlconfig.api.gui.component.IConfigEntryComponent;
import org.snakeyaml.engine.v2.comments.CommentLine;
import org.snakeyaml.engine.v2.comments.CommentType;
import org.snakeyaml.engine.v2.common.ScalarStyle;
import org.snakeyaml.engine.v2.nodes.ScalarNode;
import org.snakeyaml.engine.v2.nodes.Tag;

import java.util.Optional;
import java.util.function.Supplier;

public interface IConfigEntry<T> extends Supplier<T> {

    String getKey();

    T getDefaultValue();

    void setValue(T value);

    IComments getComments();

    IConfigEntry<T> withComments(String... comments);

    IConfigEntry<T> withComments(boolean showDefaultValues, String... comments);

    IConfigEntry<T> withComments(boolean showDefaultValues, boolean showValidationParameters, String... comments);

    void validate(T value) throws ConfigEntryValidationException;

    IConfigEntryType<IConfigEntry<T>, T> getType();

    boolean shouldBeSynced();

    IConfigEntry<T> dontSync();

    IConfigEntry<T> setShouldBeSynced(boolean shouldBeSynced);

    IConfigEntryComponent<?, ?> createComponent(String key);

    default ScalarNode createKeyNode() {
        ScalarNode keyNode = new ScalarNode(Tag.STR, getKey(), ScalarStyle.PLAIN);
        keyNode.setBlockComments(getComments().getComments().stream()
                .map(c -> new CommentLine(Optional.empty(), Optional.empty(), c, CommentType.BLOCK))
                .toList());
        return keyNode;
    }
}
