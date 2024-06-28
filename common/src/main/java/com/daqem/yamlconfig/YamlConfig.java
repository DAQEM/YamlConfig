package com.daqem.yamlconfig;

import com.daqem.yamlconfig.api.IConfig;
import com.daqem.yamlconfig.event.PlayerJoinEvent;
import com.daqem.yamlconfig.networking.YamlConfigNetworking;
import com.daqem.yamlconfig.test.TestConfig;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class YamlConfig {
    public static final String MOD_ID = "yamlconfig";
    public static final Logger LOGGER = LogUtils.getLogger();

    // Map with mod id as key and map with config name as key and IConfig as value
    public static final Map<String, Map<String, IConfig>> CONFIGS = new HashMap<>();

    public static void init() {
        TestConfig.init();
        YamlConfigNetworking.init();
        registerEvents();
    }

    public static void registerEvents() {
        PlayerJoinEvent.registerEvent();
    }

    public static ResourceLocation getId(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
