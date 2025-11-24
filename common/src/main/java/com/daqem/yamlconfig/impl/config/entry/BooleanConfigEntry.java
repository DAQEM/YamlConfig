package com.daqem.yamlconfig.impl.config.entry;

import com.daqem.yamlconfig.api.config.entry.comment.IComments;
import com.daqem.yamlconfig.api.config.entry.IBooleanConfigEntry;
import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.config.entry.serializer.IConfigEntrySerializer;
import com.daqem.yamlconfig.api.config.entry.type.IConfigEntryType;
import com.daqem.yamlconfig.api.exception.ConfigEntryValidationException;
import com.daqem.yamlconfig.api.node.IConfigNode;
import com.daqem.yamlconfig.api.node.IMapNode;
import com.daqem.yamlconfig.api.node.IValueNode;
import com.daqem.yamlconfig.impl.config.entry.type.ConfigEntryTypes;
import com.daqem.yamlconfig.impl.node.ConfigValueNode;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.snakeyaml.engine.v2.common.ScalarStyle;
import org.snakeyaml.engine.v2.nodes.NodeTuple;
import org.snakeyaml.engine.v2.nodes.ScalarNode;
import org.snakeyaml.engine.v2.nodes.Tag;

public class BooleanConfigEntry extends BaseConfigEntry<Boolean> implements IBooleanConfigEntry {

    public BooleanConfigEntry(String key, Boolean defaultValue) {
        super(key, defaultValue);
    }

    @Override
    public void validate(Boolean value) throws ConfigEntryValidationException {
    }

    @Override
    public IConfigEntryType<IConfigEntry<Boolean>, Boolean> getType() {
        //noinspection unchecked
        return (IConfigEntryType<IConfigEntry<Boolean>, Boolean>) (IConfigEntryType<?, ?>) ConfigEntryTypes.BOOLEAN;
    }

    @Override
    public IComments getComments() {
        IComments comments = super.getComments();
        if (comments.showDefaultValues()) {
            comments.addDefaultValues(getDefaultValue().toString());
        }
        return comments;
    }

    public static class Serializer implements IConfigEntrySerializer<IBooleanConfigEntry, Boolean> {

        @Override
        public void toNode(IBooleanConfigEntry configEntry, IMapNode parentMap) {
            ConfigValueNode<Boolean> node = new ConfigValueNode<>(configEntry.get());
            node.setComments(configEntry.getComments().getComments());
            parentMap.put(configEntry.getKey(), node);
        }

        @Override
        public void fromNode(IBooleanConfigEntry configEntry, IMapNode parentMap) {
            IConfigNode node = parentMap.get(configEntry.getKey());
            if (node instanceof IValueNode<?> valueNode) {
                Object val = valueNode.getValue();
                if (val instanceof Boolean) {
                    configEntry.set((Boolean) val);
                } else if (val instanceof String) {
                    configEntry.set(Boolean.parseBoolean((String) val));
                }
            }
        }

        @Override
        public void valueToNetwork(RegistryFriendlyByteBuf buf, IBooleanConfigEntry configEntry, Boolean value) {
            buf.writeBoolean(value);
        }

        @Override
        public Boolean valueFromNetwork(RegistryFriendlyByteBuf buf) {
            return buf.readBoolean();
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, IBooleanConfigEntry configEntry) {
            buf.writeUtf(configEntry.getKey());
            buf.writeBoolean(configEntry.getDefaultValue());
            buf.writeBoolean(configEntry.get());
            buf.writeCollection(configEntry.getComments().getComments(false), FriendlyByteBuf::writeUtf);
        }

        @Override
        public IBooleanConfigEntry fromNetwork(RegistryFriendlyByteBuf buf) {
            BooleanConfigEntry configEntry = new BooleanConfigEntry(buf.readUtf(), buf.readBoolean());
            configEntry.set(buf.readBoolean());
            buf.readList(FriendlyByteBuf::readUtf).forEach(configEntry.getComments()::addComment);
            return configEntry;
        }
    }
}
