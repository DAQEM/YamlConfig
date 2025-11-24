package com.daqem.yamlconfig.test.config.entry;

import com.daqem.yamlconfig.api.config.entry.serializer.IConfigEntrySerializer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.snakeyaml.engine.v2.common.ScalarStyle;
import org.snakeyaml.engine.v2.nodes.NodeTuple;
import org.snakeyaml.engine.v2.nodes.ScalarNode;
import org.snakeyaml.engine.v2.nodes.Tag;

public class TestConfigEntrySerializer implements IConfigEntrySerializer<TestConfigEntry, String> {

    @Override
    public void encodeNode(TestConfigEntry configEntry, NodeTuple nodeTuple) {
        if (nodeTuple.getValueNode() instanceof ScalarNode scalarNode) {
            configEntry.set(scalarNode.getValue());
        }
    }

    @Override
    public NodeTuple decodeNode(TestConfigEntry configEntry) {
        ScalarNode keyNode = configEntry.createKeyNode();
        ScalarNode valueNode = new ScalarNode(Tag.STR, configEntry.get(), ScalarStyle.DOUBLE_QUOTED);
        return new NodeTuple(keyNode, valueNode);
    }

    @Override
    public void valueToNetwork(RegistryFriendlyByteBuf buf, TestConfigEntry configEntry, String value) {
        buf.writeUtf(value);
    }

    @Override
    public String valueFromNetwork(RegistryFriendlyByteBuf buf) {
        return buf.readUtf();
    }

    @Override
    public void toNetwork(RegistryFriendlyByteBuf buf, TestConfigEntry configEntry) {
        buf.writeUtf(configEntry.getKey());
        buf.writeUtf(configEntry.getDefaultValue());
        buf.writeUtf(configEntry.get());
        buf.writeCollection(configEntry.getComments().getComments(false), FriendlyByteBuf::writeUtf);
    }

    @Override
    public TestConfigEntry fromNetwork(RegistryFriendlyByteBuf buf) {
        TestConfigEntry entry = new TestConfigEntry(buf.readUtf(), buf.readUtf());
        entry.set(buf.readUtf());
        buf.readList(FriendlyByteBuf::readUtf).forEach(entry.getComments()::addComment);
        return entry;
    }
}