package com.daqem.yamlconfig.impl.config.entry;

import com.daqem.yamlconfig.api.config.entry.comment.IComments;
import com.daqem.yamlconfig.api.config.entry.IBooleanConfigEntry;
import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.config.entry.serializer.IConfigEntrySerializer;
import com.daqem.yamlconfig.api.config.entry.type.IConfigEntryType;
import com.daqem.yamlconfig.api.exception.ConfigEntryValidationException;
import com.daqem.yamlconfig.api.gui.component.IConfigEntryComponent;
import com.daqem.yamlconfig.client.gui.component.entry.BooleanConfigEntryComponent;
import com.daqem.yamlconfig.impl.config.entry.type.ConfigEntryTypes;
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
    public IConfigEntryComponent<?, ?> createComponent(String key) {
        return new BooleanConfigEntryComponent(key, this);
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
        public void encodeNode(IBooleanConfigEntry configEntry, NodeTuple nodeTuple) {
            if (nodeTuple.getValueNode() instanceof ScalarNode scalarNode && scalarNode.getTag().equals(Tag.BOOL)) {
                configEntry.setValue(Boolean.parseBoolean(scalarNode.getValue()));
            }
        }

        @Override
        public NodeTuple decodeNode(IBooleanConfigEntry configEntry) {
            ScalarNode keyNode = configEntry.createKeyNode();
            ScalarNode valueNode = new ScalarNode(Tag.BOOL, Boolean.toString(configEntry.get()), ScalarStyle.PLAIN);
            return new NodeTuple(keyNode, valueNode);
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, IBooleanConfigEntry configEntry, Boolean value) {
            buf.writeBoolean(value);
        }

        @Override
        public Boolean fromNetwork(RegistryFriendlyByteBuf buf) {
            return buf.readBoolean();
        }
    }
}
