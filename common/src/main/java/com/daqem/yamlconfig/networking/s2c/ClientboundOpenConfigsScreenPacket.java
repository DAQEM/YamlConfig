package com.daqem.yamlconfig.networking.s2c;

import com.daqem.yamlconfig.api.config.IConfig;
import com.daqem.yamlconfig.impl.config.BaseConfig;
import com.daqem.yamlconfig.networking.YamlConfigNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientboundOpenConfigsScreenPacket implements CustomPacketPayload {

    public final HashMap<String, List<IConfig>> configs;

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundOpenConfigsScreenPacket> STREAM_CODEC =
            ByteBufCodecs.map(HashMap::new, ByteBufCodecs.STRING_UTF8, IConfig.CONFIG_LIST_CODEC)
                    .map(ClientboundOpenConfigsScreenPacket::new, packet -> packet.configs);

    public ClientboundOpenConfigsScreenPacket(HashMap<String, List<IConfig>> configs) {
        this.configs = configs;
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return YamlConfigNetworking.CLIENTBOUND_OPEN_CONFIGS_SCREEN_PACKET;
    }
}