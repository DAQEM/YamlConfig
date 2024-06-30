package com.daqem.yamlconfig.client;

import com.daqem.yamlconfig.client.event.PlayerLeaveEvent;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

public class YamlConfigClient {
    public static final Logger LOGGER = LogUtils.getLogger();

    public static void init() {
        registerEvents();
    }

    public static void registerEvents() {
        PlayerLeaveEvent.registerEvent();
    }
}
