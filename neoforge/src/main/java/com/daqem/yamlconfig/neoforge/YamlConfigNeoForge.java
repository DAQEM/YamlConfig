package com.daqem.yamlconfig.neoforge;


import com.daqem.yamlconfig.YamlConfig;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(YamlConfig.MOD_ID)
public class YamlConfigNeoForge {

    public YamlConfigNeoForge(IEventBus modEventBus, ModContainer modContainer) {
        // Initialize common functionality that works on both client and server
        YamlConfig.init();
    }
}
