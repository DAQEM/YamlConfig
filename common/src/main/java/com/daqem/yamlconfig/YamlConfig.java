package com.daqem.yamlconfig;

import com.daqem.yamlconfig.api.config.IConfigManager;
import com.daqem.yamlconfig.event.PlayerJoinEvent;
import com.daqem.yamlconfig.impl.config.ConfigManager;
import com.daqem.yamlconfig.networking.YamlConfigNetworking;
import com.daqem.yamlconfig.registry.YamlConfigRegistry;
import com.daqem.yamlconfig.test.CommonTestConfig;
import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

public class YamlConfig {
    public static final String MOD_ID = "yamlconfig";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final boolean isDevelopment = false;

    public static final IConfigManager CONFIG_MANAGER = new ConfigManager();

    public static void init() {
        if (isDevelopment) {
            CommonTestConfig.init();
        }
        YamlConfigNetworking.init();
        YamlConfigRegistry.init();
        registerEvents();
    }

    public static void registerEvents() {
        PlayerJoinEvent.registerEvent();
    }

    public static ResourceLocation getId(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public static MutableComponent translatable(String key) {
        return Component.translatable(MOD_ID + "." + key);
    }

    public static MutableComponent translatable(String key, Object... args) {
        return Component.translatable(MOD_ID + "." + key, args);
    }
}
