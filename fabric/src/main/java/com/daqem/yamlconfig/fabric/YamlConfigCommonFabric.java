package com.daqem.yamlconfig.fabric;

import com.daqem.yamlconfig.YamlConfig;
import net.fabricmc.api.ModInitializer;

public class YamlConfigCommonFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        YamlConfig.init();
    }
}
