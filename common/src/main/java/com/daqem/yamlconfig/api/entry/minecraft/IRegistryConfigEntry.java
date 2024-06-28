package com.daqem.yamlconfig.api.entry.minecraft;

import com.daqem.yamlconfig.api.entry.IConfigEntry;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.snakeyaml.engine.v2.common.ScalarStyle;
import org.snakeyaml.engine.v2.nodes.NodeTuple;
import org.snakeyaml.engine.v2.nodes.ScalarNode;
import org.snakeyaml.engine.v2.nodes.Tag;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
