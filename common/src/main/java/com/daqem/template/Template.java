package com.daqem.template;

import com.mojang.logging.LogUtils;
import dev.architectury.event.events.common.TickEvent;
import org.slf4j.Logger;

public class Template {
    public static final String MOD_ID = "template";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static void init() {
        TickEvent.SERVER_PRE.register(instance -> {
            LOGGER.info("(server) This line is printed by an example mod event!");
        });
    }
}
