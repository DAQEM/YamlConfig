package com.daqem.yamlconfig.networking.c2s;

import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.api.config.IConfig;
import com.daqem.yamlconfig.networking.YamlConfigNetworking;
import com.daqem.yamlconfig.networking.s2c.ClientboundOpenConfigsScreenPacket;
import com.google.common.graph.Network;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ServerboundOpenConfigsScreenPacket implements CustomPacketPayload {

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundOpenConfigsScreenPacket> STREAM_CODEC = StreamCodec.of(
            (buf, packet) -> {
            },
            buf -> {
                return new ServerboundOpenConfigsScreenPacket();
            }
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return YamlConfigNetworking.SERVERBOUND_OPEN_CONFIGS_SCREEN_PACKET;
    }

    public void handleServerSide(NetworkManager.PacketContext packetContext) {
        List<IConfig> configs = YamlConfig.CONFIG_MANAGER.getAllServerAndCommonConfigs();
        Map<String, List<IConfig>> configMap = YamlConfig.CONFIG_MANAGER.getAllConfigs().stream()
                .collect(Collectors.groupingBy(IConfig::getModId));

        NetworkManager.sendToPlayer(
                (ServerPlayer) packetContext.getPlayer(),
                new ClientboundOpenConfigsScreenPacket(configMap)
        );
    }
}
