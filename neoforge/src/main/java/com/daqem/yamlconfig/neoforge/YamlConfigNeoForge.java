package com.daqem.yamlconfig.neoforge;


import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.YamlConfigExpectPlatform;
import dev.architectury.utils.EnvExecutor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

import java.io.File;

@Mod(YamlConfig.MOD_ID)
public class YamlConfigNeoForge {

    public YamlConfigNeoForge(IEventBus modEventBus, ModContainer modContainer) {
        YamlConfig.init();

        EnvExecutor.getEnvSpecific(
                () -> () -> new SideProxyNeoForge.Client(modEventBus, modContainer),
                () -> () -> new SideProxyNeoForge.Server(modEventBus, modContainer)
        );
    }
}
