package com.daqem.yamlconfig.neoforge.platform;

import com.daqem.yamlconfig.platform.IPlatformHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.network.PacketDistributor;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PlatformHelperNeoForge implements IPlatformHelper {

    public static final List<KeyMapping> KEYS_TO_REGISTER = new ArrayList<>();

    @Override
    public Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }

    @Override
    public void sendToServer(CustomPacketPayload payload) {
        ClientPacketDistributor.sendToServer(payload);
    }

    @Override
    public void sendToPlayer(ServerPlayer player, CustomPacketPayload payload) {
        try {
            PacketDistributor.sendToPlayer(player, payload);
        } catch (UnsupportedOperationException e) {
            // This exception is thrown by NeoForge if the client does not have the
            // channel registered (i.e., they don't have the mod installed).
            // Since our networking is optional, we catch this and ignore it.
        }
    }

    @Override
    public void registerKeyBinding(KeyMapping mapping) {
        KEYS_TO_REGISTER.add(mapping);
    }
}
