package com.daqem.yamlconfig.networking.s2c;

import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.api.IConfig;
import com.daqem.yamlconfig.api.entry.IConfigEntry;
import com.daqem.yamlconfig.networking.YamlConfigNetworking;
import dev.architectury.networking.NetworkManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.Map;

public class ClientboundSyncConfigPacket implements CustomPacketPayload {

    private final IConfig config;

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundSyncConfigPacket> STREAM_CODEC = StreamCodec.of(
            (buf, packet) -> {
                buf.writeUtf(packet.config.getModId());
                buf.writeUtf(packet.config.getName());
            },
            buf -> {
                String modId = buf.readUtf();
                String name = buf.readUtf();

                return new ClientboundSyncConfigPacket(YamlConfig.CONFIGS.get(modId).get(name));
            }
    );

    public ClientboundSyncConfigPacket(IConfig config) {
        this.config = config;
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return YamlConfigNetworking.CLIENTBOUND_SYNC_CONFIG;
    }

    @Environment(EnvType.CLIENT)
    public static void handleClientSide(ClientboundSyncConfigPacket packet, NetworkManager.PacketContext context) {
        // Handle
    }
}
