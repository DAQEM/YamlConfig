package com.daqem.yamlconfig.networking.c2s;

import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.api.config.ConfigType;
import com.daqem.yamlconfig.api.config.IConfig;
import com.daqem.yamlconfig.api.config.serializer.IConfigSerializer;
import com.daqem.yamlconfig.networking.YamlConfigNetworking;
import com.daqem.yamlconfig.networking.s2c.ClientboundOpenConfigScreenPacket;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public class ServerboundSaveConfigPacket implements CustomPacketPayload {

    private final IConfig config;

    @SuppressWarnings("unchecked")
    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundSaveConfigPacket> STREAM_CODEC = StreamCodec.of(
            (buf, packet) -> {
                buf.writeEnum(packet.config.getType());
                ((IConfigSerializer<IConfig>) packet.config.getType().getSerializer()).toNetwork(buf, packet.config);
            },
            buf -> {
                ConfigType type = buf.readEnum(ConfigType.class);
                IConfig config = type.getSerializer().fromNetwork(buf);
                return new ServerboundSaveConfigPacket(config);
            }
    );

    public ServerboundSaveConfigPacket(IConfig config) {
        this.config = config;
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return YamlConfigNetworking.SERVERBOUND_SAVE_CONFIG_PACKET;
    }

    public void handleServerSide(NetworkManager.PacketContext packetContext) {
        if (packetContext.getPlayer().hasPermissions(2)) {
            IConfig existingConfig = YamlConfig.CONFIG_MANAGER.getConfig(config.getModId(), config.getName());
            existingConfig.updateEntries(config.getEntries());
            existingConfig.save();
        }
    }
}
