package com.daqem.yamlconfig.event;

import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.networking.s2c.ClientboundSyncConfigPacket;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.networking.NetworkManager;

public class PlayerJoinEvent {

    public static void registerEvent() {
        PlayerEvent.PLAYER_JOIN.register(player ->
                YamlConfig.CONFIG_MANAGER.getAllCommonConfigs().forEach(iConfig ->
                        NetworkManager.sendToPlayer(player, new ClientboundSyncConfigPacket(iConfig))
                )
        );
    }
}
