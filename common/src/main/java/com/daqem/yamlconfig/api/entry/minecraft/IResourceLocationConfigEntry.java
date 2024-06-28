package com.daqem.yamlconfig.api.entry.minecraft;

import com.daqem.yamlconfig.api.entry.IConfigEntry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import org.snakeyaml.engine.v2.common.ScalarStyle;
import org.snakeyaml.engine.v2.nodes.NodeTuple;
import org.snakeyaml.engine.v2.nodes.ScalarNode;
import org.snakeyaml.engine.v2.nodes.Tag;

public interface IResourceLocationConfigEntry extends IConfigEntry<ResourceLocation> {

    static StreamCodec<IResourceLocationConfigEntry, NodeTuple> createCodec() {
        return StreamCodec.of(
                (resourceLocationConfigEntry, node) -> {
                    if (node.getValueNode() instanceof ScalarNode scalarNode && scalarNode.getTag().equals(Tag.STR)) {
                        resourceLocationConfigEntry.setValue(ResourceLocation.parse(scalarNode.getValue()));
                    }
                },
                resourceLocationConfigEntry -> {
                    ScalarNode keyNode = resourceLocationConfigEntry.createKeyNode();
                    ScalarNode valueNode = new ScalarNode(Tag.STR, resourceLocationConfigEntry.getValue().toString(), ScalarStyle.SINGLE_QUOTED);
                    return new NodeTuple(keyNode, valueNode);
                }
        );
    }

    String getPattern();

    @Override
    default <B extends IConfigEntry<ResourceLocation>> StreamCodec<B, NodeTuple> getCodec() {
        //noinspection unchecked
        return (StreamCodec<B, NodeTuple>) createCodec();
    }
}
