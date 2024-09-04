package com.daqem.yamlconfig.networking.s2c;

import com.daqem.yamlconfig.api.config.IConfig;
import com.daqem.yamlconfig.client.gui.screen.ConfigsScreen;
import com.daqem.yamlconfig.impl.config.BaseConfig;
import com.daqem.yamlconfig.networking.YamlConfigNetworking;
import dev.architectury.networking.NetworkManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class ClientboundOpenConfigsScreenPacket implements CustomPacketPayload {

    private final Map<String, List<IConfig>> configs;

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundOpenConfigsScreenPacket> STREAM_CODEC = StreamCodec.of(
            (buf, packet) ->
                    buf.writeMap(packet.configs,
                            FriendlyByteBuf::writeUtf,
                            (buf1, configs) ->
                                    buf1.writeCollection(configs, (buf2, config) ->
                                            BaseConfig.STREAM_CODEC.encode((RegistryFriendlyByteBuf) buf2, (BaseConfig) config))
                    ),
            buf ->
                    new ClientboundOpenConfigsScreenPacket(
                            buf.readMap(
                                    FriendlyByteBuf::readUtf,
                                    buf1 -> buf1.readList(buf2 ->
                                            BaseConfig.STREAM_CODEC.decode((RegistryFriendlyByteBuf) buf2))
                            )
                    )
    );

    public ClientboundOpenConfigsScreenPacket(Map<String, List<IConfig>> configs) {
        this.configs = configs;
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return YamlConfigNetworking.CLIENTBOUND_OPEN_CONFIGS_SCREEN_PACKET;
    }

    @Environment(EnvType.CLIENT)
    public void handleClientSide(NetworkManager.PacketContext packetContext) {
        Minecraft.getInstance().setScreen(new ConfigsScreen(configs));
    }
}
