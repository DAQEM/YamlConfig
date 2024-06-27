package com.daqem.yamlconfig.api.entry.minecraft;

import com.daqem.yamlconfig.api.entry.IConfigEntry;
import com.daqem.yamlconfig.api.entry.IEnumConfigEntry;
import net.minecraft.core.Registry;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import org.snakeyaml.engine.v2.common.ScalarStyle;
import org.snakeyaml.engine.v2.nodes.NodeTuple;
import org.snakeyaml.engine.v2.nodes.ScalarNode;
import org.snakeyaml.engine.v2.nodes.Tag;

import java.util.Objects;

public interface IRegistryConfigEntry<T> extends IConfigEntry<T> {

    static <T> StreamCodec<IRegistryConfigEntry<T>, NodeTuple> createCodec() {
        return StreamCodec.of(
                (registryConfigEntry, node) -> {
                    if (node.getValueNode() instanceof ScalarNode scalarNode && scalarNode.getTag().equals(Tag.STR)) {
                        ResourceLocation resourceLocation = ResourceLocation.parse(scalarNode.getValue());
                        registryConfigEntry.setValue(registryConfigEntry.getRegistry().get(resourceLocation));
                    }
                },
                registryConfigEntry -> {
                    ScalarNode keyNode = registryConfigEntry.createKeyNode();
                    ResourceLocation key = registryConfigEntry.getRegistry().getKey(registryConfigEntry.getValue());
                    ScalarNode valueNode = new ScalarNode(Tag.STR, Objects.requireNonNull(key).toString(), ScalarStyle.SINGLE_QUOTED);
                    return new NodeTuple(keyNode, valueNode);
                }
        );
    }

    Registry<T> getRegistry();

    @Override
    default <B extends IConfigEntry<T>> StreamCodec<B, NodeTuple> getCodec() {
        //noinspection unchecked
        return (StreamCodec<B, NodeTuple>) createCodec();
    }
}
