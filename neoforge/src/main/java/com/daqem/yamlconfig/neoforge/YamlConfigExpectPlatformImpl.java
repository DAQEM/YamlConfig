package com.daqem.yamlconfig.neoforge;

import net.minecraft.client.KeyMapping;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

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
        try {
            ClientPacketDistributor.sendToServer(payload);
        } catch (UnsupportedOperationException e) {
            // The server doesn't have the mod installed. Ignore.
        } catch (Exception e) {
            // Catch any other potential networking issues
        }
    }

    public static void registerKeyBinding(KeyMapping mapping) {
        KEYS_TO_REGISTER.add(mapping);
    }
}
