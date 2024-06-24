package com.daqem.template.client;

import com.mojang.logging.LogUtils;
import dev.architectury.event.events.client.ClientTickEvent;
import org.slf4j.Logger;

public class TemplateClient {
    public static final Logger LOGGER = LogUtils.getLogger();

    public static void init() {
        ClientTickEvent.CLIENT_LEVEL_PRE.register(instance -> {
            LOGGER.info("(client) This line is printed by an example mod event!");
        });
    }
}
