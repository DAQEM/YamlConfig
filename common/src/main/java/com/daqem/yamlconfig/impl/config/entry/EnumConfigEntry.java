package com.daqem.yamlconfig.impl.config.entry;

import com.daqem.yamlconfig.api.config.entry.comment.IComments;
import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.config.entry.IEnumConfigEntry;
import com.daqem.yamlconfig.api.config.entry.serializer.IConfigEntrySerializer;
import com.daqem.yamlconfig.api.config.entry.type.IConfigEntryType;
import com.daqem.yamlconfig.api.exception.ConfigEntryValidationException;
import com.daqem.yamlconfig.impl.config.entry.type.ConfigEntryTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.snakeyaml.engine.v2.common.ScalarStyle;
import org.snakeyaml.engine.v2.nodes.NodeTuple;
import org.snakeyaml.engine.v2.nodes.ScalarNode;
import org.snakeyaml.engine.v2.nodes.Tag;

import java.util.stream.Stream;

public class EnumConfigEntry<E extends Enum<E>> extends BaseConfigEntry<E> implements IEnumConfigEntry<E> {

    private final Class<E> enumClass;

    public EnumConfigEntry(String key, E defaultValue, Class<E> enumClass) {
        super(key, defaultValue);
        this.enumClass = enumClass;
    }

    @Override
    public void validate(E value) throws ConfigEntryValidationException {
        if (value == null) {
            throw new ConfigEntryValidationException(getKey(), "Value cannot be null");
        }
    }

    @Override
    public IConfigEntryType<IConfigEntry<E>, E> getType() {
        //noinspection unchecked
        return (IConfigEntryType<IConfigEntry<E>, E>) (IConfigEntryType<?, ?>) ConfigEntryTypes.ENUM;
    }

    @Override
    public Class<E> getEnumClass() {
        return enumClass;
    }

    @Override
    public IComments getComments() {
        IComments comments = super.getComments();
        if (comments.showValidationParameters()) {
            comments.addValidationParameter("Allowed values: " + Stream.of(getEnumClass().getEnumConstants()).map(s -> "'" + s + "'").toList());
        }
        if (comments.showDefaultValues()) {
            comments.addDefaultValues(getDefaultValue().toString());
        }
        return comments;
    }

    public static class Serializer<E extends Enum<E>> implements IConfigEntrySerializer<IEnumConfigEntry<E>, E> {

        @Override
        public void encodeNode(IEnumConfigEntry<E> configEntry, NodeTuple nodeTuple) {
            if (nodeTuple.getValueNode() instanceof ScalarNode scalarNode && scalarNode.getTag().equals(Tag.STR)) {
                configEntry.setValue(Enum.valueOf(configEntry.getEnumClass(), scalarNode.getValue()));
            }
        }

        @Override
        public NodeTuple decodeNode(IEnumConfigEntry<E> configEntry) {
            ScalarNode keyNode = configEntry.createKeyNode();
            ScalarNode valueNode = new ScalarNode(Tag.STR, configEntry.get().toString(), ScalarStyle.SINGLE_QUOTED);
            return new NodeTuple(keyNode, valueNode);
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, IEnumConfigEntry<E> configEntry, E value) {
            buf.writeUtf(configEntry.getEnumClass().getName());
            buf.writeEnum(value);
        }

        @Override
        public E fromNetwork(RegistryFriendlyByteBuf buf) {
            try {
                //noinspection unchecked
                Class<E> enumClass = (Class<E>) Class.forName(buf.readUtf());
                return buf.readEnum(enumClass);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
