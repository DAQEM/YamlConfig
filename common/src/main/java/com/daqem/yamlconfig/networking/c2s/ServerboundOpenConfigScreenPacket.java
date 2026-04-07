package com.daqem.yamlconfig.networking.c2s;

import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.api.config.IConfig;
import com.daqem.yamlconfig.networking.YamlConfigNetworking;
import com.daqem.yamlconfig.networking.s2c.ClientboundOpenConfigScreenPacket;
import com.daqem.yamlconfig.platform.Services;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permission;
import net.minecraft.server.permissions.PermissionLevel;
import org.jetbrains.annotations.NotNull;

public class ServerboundOpenConfigScreenPacket implements CustomPacketPayload {

    private final String modId;
    private final String configName;

    public final IConfig config;

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundOpenConfigScreenPacket> STREAM_CODEC = StreamCodec.of(
            (buf, packet) -> {
                buf.writeUtf(packet.modId);
                buf.writeUtf(packet.configName);
            },
            buf -> new ServerboundOpenConfigScreenPacket(YamlConfig.CONFIG_MANAGER.getConfig(buf.readUtf(), buf.readUtf()))
    );

    public ServerboundOpenConfigScreenPacket(String modId, String configName) {
        this.modId = modId;
        this.configName = configName;
        this.config = null;
    }

    public ServerboundOpenConfigScreenPacket(IConfig config) {
        this.modId = config.getModId();
        this.configName = config.getName();
        this.config = config;
    }

    @Override
    public @NotNull Type<? extends @NotNull CustomPacketPayload> type() {
        return YamlConfigNetworking.SERVERBOUND_OPEN_CONFIG_SCREEN_PACKET;
    }

    public void handleServerSide(ServerPlayer serverPlayer) {

        if (serverPlayer.permissions().hasPermission(new Permission.HasCommandLevel(PermissionLevel.byId(2)))) {
            Services.PLATFORM.sendToPlayer(
                    serverPlayer,
                    new ClientboundOpenConfigScreenPacket(this.config)
            );
        }
    }
}
