package com.daqem.yamlconfig.fabric;

import com.daqem.yamlconfig.client.YamlConfigClient;
import net.fabricmc.api.ClientModInitializer;

public class YamlConfigClientFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        YamlConfigClient.init();
    }
}
