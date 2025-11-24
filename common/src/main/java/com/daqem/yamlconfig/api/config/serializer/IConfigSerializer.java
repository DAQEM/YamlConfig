package com.daqem.yamlconfig.api.config.serializer;

import com.daqem.yamlconfig.api.config.IConfig;
import net.minecraft.network.RegistryFriendlyByteBuf;

/**
 * Serializer interface for serializing and deserializing configuration objects to and from network buffers.
 *
 * @param <T> the type of configuration object
 */
public interface IConfigSerializer<T extends IConfig> {

    /**
     * Serializes the given configuration object to the provided network buffer.
     *
     * @param buf    the network buffer to write to
     * @param config the configuration object to serialize
     */
    void toNetwork(RegistryFriendlyByteBuf buf, T config);

    /**
     * Deserializes a configuration object from the provided network buffer.
     *
     * @param buf the network buffer to read from
     * @return the deserialized configuration object
     */
    T fromNetwork(RegistryFriendlyByteBuf buf);
}
