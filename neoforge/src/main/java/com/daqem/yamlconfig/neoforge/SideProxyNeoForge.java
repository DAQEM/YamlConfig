package com.daqem.yamlconfig.neoforge;

import com.daqem.yamlconfig.client.YamlConfigClient;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import org.slf4j.Logger;

public class SideProxyNeoForge {

    private static final Logger LOGGER = LogUtils.getLogger();

    public SideProxyNeoForge(IEventBus modEventBus, ModContainer modContainer) {
        //Run common code
    }

    public static class Client extends SideProxyNeoForge {

        public Client(IEventBus modEventBus, ModContainer modContainer) {
            super(modEventBus, modContainer);
            YamlConfigClient.init();
            //Run client code
        }

    }

    public static class Server extends SideProxyNeoForge {

        public Server(IEventBus modEventBus, ModContainer modContainer) {
            super(modEventBus, modContainer);
            //Run server code
        }
    }
}
