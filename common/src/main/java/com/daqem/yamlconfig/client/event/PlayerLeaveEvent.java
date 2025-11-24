package com.daqem.yamlconfig.client.event;

import com.daqem.yamlconfig.YamlConfig;

public class PlayerLeaveEvent {

    public static void onPlayerLeave() {
        YamlConfig.CONFIG_MANAGER.reloadSyncedConfigs();
    }
}