package com.daqem.yamlconfig.api.entry;

import com.daqem.yamlconfig.api.exception.ConfigEntryValidationException;
import net.minecraft.network.codec.StreamCodec;
import org.snakeyaml.engine.v2.nodes.Node;

public interface IConfigEntry<T> {

    String getKey();

    T getDefaultValue();

    T getValue();

    void setValue(T value);

    void validate(T value) throws ConfigEntryValidationException;

    <B extends IConfigEntry<T>>StreamCodec<B, Node> getCodec();

    default void encode(Node node) {
        getCodec().encode(this, node);
    }

    default Node decode() {
        return getCodec().decode(this);
    }
}
