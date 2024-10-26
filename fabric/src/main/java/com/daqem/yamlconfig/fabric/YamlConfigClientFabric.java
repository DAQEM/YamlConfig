package com.daqem.yamlconfig.fabric;

import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.client.YamlConfigClient;
import com.daqem.yamlconfig.test.ClientTestConfig;
import net.fabricmc.api.ClientModInitializer;

public class YamlConfigClientFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        YamlConfigClient.init();

        if (YamlConfig.isDevelopment) {
            ClientTestConfig.init();
        }
    }
}
