package com.daqem.yamlconfig.networking.s2c;

import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.api.config.IConfig;
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

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClientboundOpenConfigScreenPacket implements CustomPacketPayload {

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundOpenConfigScreenPacket> STREAM_CODEC = StreamCodec.of(
            (buf, packet) -> {
            },
            buf -> {
                return new ClientboundOpenConfigScreenPacket();
            }
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return YamlConfigNetworking.CLIENTBOUND_OPEN_CONFIG_SCREEN_PACKET;
    }

    @Environment(EnvType.CLIENT)
    public void handleClientSide(NetworkManager.PacketContext packetContext) {
        Map<String, List<IConfig>> configs = YamlConfig.CONFIG_MANAGER.getAllConfigs().stream()
                .collect(Collectors.groupingBy(IConfig::getModId));

        Minecraft.getInstance().setScreen(new ConfigScreen(Minecraft.getInstance().screen, configs.get("test").getFirst()));
    }
}
