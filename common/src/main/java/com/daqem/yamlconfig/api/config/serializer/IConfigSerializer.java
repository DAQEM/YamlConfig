package com.daqem.yamlconfig.api.config.serializer;

import com.daqem.yamlconfig.api.config.IConfig;
import net.minecraft.network.RegistryFriendlyByteBuf;

public interface IConfigSerializer<T extends IConfig> {

    void toNetwork(RegistryFriendlyByteBuf buf, T config);

    T fromNetwork(RegistryFriendlyByteBuf buf);
}
