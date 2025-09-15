package com.daqem.yamlconfig.neoforge;

import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.client.YamlConfigClient;
import com.daqem.yamlconfig.networking.YamlConfigNetworking;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = YamlConfig.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class YamlConfigNeoForgeClient {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        // Initialize client-specific functionality
        YamlConfigClient.init();
        
        // Initialize full client and server networking on client
        YamlConfigNetworking.initClient();
        YamlConfigNetworking.initServer();
    }
}