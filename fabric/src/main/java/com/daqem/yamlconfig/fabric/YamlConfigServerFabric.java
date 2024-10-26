package com.daqem.yamlconfig.fabric;

import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.test.ServerTestConfig;
import net.fabricmc.api.DedicatedServerModInitializer;

public class YamlConfigServerFabric implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        if (YamlConfig.isDevelopment) {
            ServerTestConfig.init();
        }
    }
}
