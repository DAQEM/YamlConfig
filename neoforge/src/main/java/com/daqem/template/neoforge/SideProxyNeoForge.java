package com.daqem.template.neoforge;

import com.daqem.template.Template;
import com.daqem.template.client.TemplateClient;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import org.slf4j.Logger;

public class SideProxyNeoForge {

    private static final Logger LOGGER = LogUtils.getLogger();

    public SideProxyNeoForge(IEventBus modEventBus, ModContainer modContainer) {
        Template.init();
        //Run common code
        LOGGER.info("Running common code");
    }

    public static class Client extends SideProxyNeoForge {

        public Client(IEventBus modEventBus, ModContainer modContainer) {
            super(modEventBus, modContainer);
            TemplateClient.init();
            //Run client code
            LOGGER.info("Running client code");
        }

    }

    public static class Server extends SideProxyNeoForge {

        public Server(IEventBus modEventBus, ModContainer modContainer) {
            super(modEventBus, modContainer);
            //Run server code
            LOGGER.info("Running server code");
        }
    }
}
