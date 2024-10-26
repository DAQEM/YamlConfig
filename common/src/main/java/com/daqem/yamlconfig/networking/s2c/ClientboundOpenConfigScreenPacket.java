package com.daqem.yamlconfig.networking.s2c;

import com.daqem.yamlconfig.api.config.ConfigType;
import com.daqem.yamlconfig.api.config.IConfig;
import com.daqem.yamlconfig.api.config.serializer.IConfigSerializer;
import com.daqem.yamlconfig.client.gui.screen.ConfigScreen;
import com.daqem.yamlconfig.networking.YamlConfigNetworking;
import dev.architectury.networking.NetworkManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public class ClientboundOpenConfigScreenPacket implements CustomPacketPayload {

    private final IConfig config;

    @SuppressWarnings("unchecked")
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundOpenConfigScreenPacket> STREAM_CODEC = StreamCodec.of(
            (buf, packet) -> {
                buf.writeEnum(packet.config.getType());
                ((IConfigSerializer<IConfig>) packet.config.getType().getSerializer()).toNetwork(buf, packet.config);
            },
            buf -> {
                ConfigType type = buf.readEnum(ConfigType.class);
                IConfig config = type.getSerializer().fromNetwork(buf);
                return new ClientboundOpenConfigScreenPacket(config);
            }
    );

    public ClientboundOpenConfigScreenPacket(IConfig config) {
        this.config = config;
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return YamlConfigNetworking.CLIENTBOUND_OPEN_CONFIG_SCREEN_PACKET;
    }

    @Environment(EnvType.CLIENT)
    public void handleClientSide(NetworkManager.PacketContext packetContext) {
        Minecraft.getInstance().setScreen(new ConfigScreen(Minecraft.getInstance().screen, this.config));
    }
}
