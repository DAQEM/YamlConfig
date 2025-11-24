package com.daqem.yamlconfig.event;

import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.YamlConfigExpectPlatform;
import com.daqem.yamlconfig.networking.s2c.ClientboundSyncConfigPacket;
import net.minecraft.server.level.ServerPlayer;

public class PlayerJoinEvent {

    public static void onPlayerJoin(ServerPlayer player) {
        YamlConfig.CONFIG_MANAGER.getAllCommonConfigs().forEach(iConfig ->
                YamlConfigExpectPlatform.sendToPlayer(player, new ClientboundSyncConfigPacket(iConfig))
        );
    }
}