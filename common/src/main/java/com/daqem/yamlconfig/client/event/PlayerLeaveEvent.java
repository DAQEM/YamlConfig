package com.daqem.yamlconfig.client.event;

import com.daqem.yamlconfig.YamlConfig;
import dev.architectury.event.events.client.ClientPlayerEvent;

public class PlayerLeaveEvent {

    public static void registerEvent() {
        ClientPlayerEvent.CLIENT_PLAYER_QUIT.register(player ->
                YamlConfig.CONFIG_MANAGER.reloadSyncedConfigs());
    }
}
