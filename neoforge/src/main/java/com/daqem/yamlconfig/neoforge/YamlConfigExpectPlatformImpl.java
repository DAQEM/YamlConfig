package com.daqem.yamlconfig.neoforge;

import net.minecraft.client.KeyMapping;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.network.PacketDistributor;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class YamlConfigExpectPlatformImpl {

    public static final List<KeyMapping> KEYS_TO_REGISTER = new ArrayList<>();

    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }

    public static void sendToServer(CustomPacketPayload payload) {
        ClientPacketDistributor.sendToServer(payload);
    }

    public static void sendToPlayer(ServerPlayer player, CustomPacketPayload payload) {
        PacketDistributor.sendToPlayer(player, payload);
    }

    public static void registerKeyBinding(KeyMapping mapping) {
        KEYS_TO_REGISTER.add(mapping);
    }
}
