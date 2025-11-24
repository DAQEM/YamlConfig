package com.daqem.yamlconfig;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

import java.nio.file.Path;

public class YamlConfigExpectPlatform {

    @ExpectPlatform
    public static Path getConfigDirectory() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void sendToServer(CustomPacketPayload payload) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void sendToPlayer(ServerPlayer player, CustomPacketPayload payload) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void registerKeyBinding(KeyMapping mapping) {
        throw new AssertionError();
    }
}
