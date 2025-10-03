package com.daqem.yamlconfig.client;

import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.client.event.KeyPressEvent;
import com.daqem.yamlconfig.client.event.PlayerLeaveEvent;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.logging.LogUtils;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import net.minecraft.client.KeyMapping;
import org.slf4j.Logger;

public class YamlConfigClient {
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final KeyMapping.Category YAML_CONFIG_CATEGORY = new KeyMapping.Category(YamlConfig.getId("category"));
    public static final KeyMapping CONFIGS_KEY = new KeyMapping("key.yamlconfig.configs", InputConstants.Type.KEYSYM, InputConstants.KEY_F12, YAML_CONFIG_CATEGORY);

    public static void init() {
        KeyMappingRegistry.register(CONFIGS_KEY);
        registerEvents();
    }

    public static void registerEvents() {
        PlayerLeaveEvent.registerEvent();
        KeyPressEvent.registerEvent();
    }
}
