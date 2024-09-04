package com.daqem.yamlconfig.networking.c2s;

import com.daqem.yamlconfig.networking.YamlConfigNetworking;
import com.daqem.yamlconfig.networking.s2c.ClientboundOpenConfigScreenPacket;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public class ServerboundOpenConfigScreenPacket implements CustomPacketPayload {

    private final String modId;
    private final String configName;

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundOpenConfigScreenPacket> STREAM_CODEC = StreamCodec.of(
            (buf, packet) -> {
                buf.writeUtf(packet.modId);
                buf.writeUtf(packet.configName);
            },
            buf -> new ServerboundOpenConfigScreenPacket(buf.readUtf(), buf.readUtf())
    );

    public ServerboundOpenConfigScreenPacket(String modId, String configName) {
        this.modId = modId;
        this.configName = configName;
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return YamlConfigNetworking.SERVERBOUND_OPEN_CONFIG_SCREEN_PACKET;
    }

    public void handleServerSide(NetworkManager.PacketContext packetContext) {

        NetworkManager.sendToPlayer(
                (ServerPlayer) packetContext.getPlayer(),
                new ClientboundOpenConfigScreenPacket()
        );
    }
}
