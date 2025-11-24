package com.daqem.yamlconfig.test.config.entry;

import com.daqem.yamlconfig.api.config.entry.serializer.IConfigEntrySerializer;
import com.daqem.yamlconfig.api.node.IConfigNode;
import com.daqem.yamlconfig.api.node.IMapNode;
import com.daqem.yamlconfig.api.node.IValueNode;
import com.daqem.yamlconfig.impl.node.ConfigValueNode;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;

public class TestConfigEntrySerializer implements IConfigEntrySerializer<TestConfigEntry, String> {

    @Override
    public void toNode(TestConfigEntry configEntry, IMapNode parentMap) {
        ConfigValueNode<String> node = new ConfigValueNode<>(configEntry.get());
        node.setComments(configEntry.getComments().getComments());
        parentMap.put(configEntry.getKey(), node);
    }

    @Override
    public void fromNode(TestConfigEntry configEntry, IMapNode parentMap) {
        IConfigNode node = parentMap.get(configEntry.getKey());
        if (node instanceof IValueNode<?> valueNode && valueNode.getValue() != null) {
            configEntry.set(valueNode.getValue().toString());
        }
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