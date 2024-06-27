package com.daqem.yamlconfig.networking.s2c;

import com.daqem.yamlconfig.networking.YamlConfigNetworking;
import dev.architectury.networking.NetworkManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public class ClientboundSyncConfigPacket implements CustomPacketPayload {

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundSyncConfigPacket> STREAM_CODEC = StreamCodec.of(
            (friendlyByteBuf, packet) -> {
                // Write
            },
            friendlyByteBuf -> {
                // Read
                return new ClientboundSyncConfigPacket();
            }
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return YamlConfigNetworking.CLIENTBOUND_SYNC_CONFIG;
    }

    @Environment(EnvType.CLIENT)
    public static void handleClientSide(ClientboundSyncConfigPacket packet, NetworkManager.PacketContext context) {
        // Handle
    }
}
