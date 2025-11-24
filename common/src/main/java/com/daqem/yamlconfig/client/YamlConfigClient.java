package com.daqem.yamlconfig.client;

import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.api.config.entry.IEnumConfigEntry;
import com.daqem.yamlconfig.client.event.KeyPressEvent;
import com.daqem.yamlconfig.client.event.PlayerLeaveEvent;
import com.daqem.yamlconfig.client.gui.component.entry.*;
import com.daqem.yamlconfig.client.gui.component.entry.list.*;
import com.daqem.yamlconfig.client.gui.component.entry.list.numeric.*;
import com.daqem.yamlconfig.client.gui.component.entry.map.*;
import com.daqem.yamlconfig.client.gui.component.entry.map.numeric.*;
import com.daqem.yamlconfig.client.gui.component.entry.minecraft.*;
import com.daqem.yamlconfig.client.gui.component.entry.numeric.*;
import com.daqem.yamlconfig.client.gui.registry.ConfigEntryComponentRegistry;
import com.daqem.yamlconfig.impl.config.entry.*;
import com.daqem.yamlconfig.impl.config.entry.minecraft.*;
import com.daqem.yamlconfig.impl.config.entry.type.ConfigEntryTypes;
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
        registerComponents();
    }

    public static void registerEvents() {
        PlayerLeaveEvent.registerEvent();
        KeyPressEvent.registerEvent();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void registerComponents() {
        ConfigEntryComponentRegistry.register(ConfigEntryTypes.BOOLEAN, BooleanConfigEntryComponent::new);
        ConfigEntryComponentRegistry.register(ConfigEntryTypes.DATE_TIME, DateTimeConfigEntryComponent::new);
        ConfigEntryComponentRegistry.register(ConfigEntryTypes.ENUM, (key, entry) -> new EnumConfigEntryComponent(key, entry));
        ConfigEntryComponentRegistry.register(ConfigEntryTypes.STRING, StringConfigEntryComponent::new);
        ConfigEntryComponentRegistry.register(ConfigEntryTypes.STRING_LIST, StringListConfigEntryComponent::new);
        ConfigEntryComponentRegistry.register(ConfigEntryTypes.DOUBLE_LIST, DoubleListConfigEntryComponent::new);
        ConfigEntryComponentRegistry.register(ConfigEntryTypes.FLOAT_LIST, FloatListConfigEntryComponent::new);
        ConfigEntryComponentRegistry.register(ConfigEntryTypes.INTEGER_LIST, IntegerListConfigEntryComponent::new);
        ConfigEntryComponentRegistry.register(ConfigEntryTypes.STRING_MAP, StringMapConfigEntryComponent::new);
        ConfigEntryComponentRegistry.register(ConfigEntryTypes.DOUBLE_MAP, DoubleMapConfigEntryComponent::new);
        ConfigEntryComponentRegistry.register(ConfigEntryTypes.FLOAT_MAP, FloatMapConfigEntryComponent::new);
        ConfigEntryComponentRegistry.register(ConfigEntryTypes.INTEGER_MAP, IntegerMapConfigEntryComponent::new);
        ConfigEntryComponentRegistry.register(ConfigEntryTypes.REGISTRY, (key, entry) -> new RegistryConfigEntryComponent(key, entry));
        ConfigEntryComponentRegistry.register(ConfigEntryTypes.RESOURCE_LOCATION, ResourceLocationConfigEntryComponent::new);
        ConfigEntryComponentRegistry.register(ConfigEntryTypes.DOUBLE, DoubleConfigEntryComponent::new);
        ConfigEntryComponentRegistry.register(ConfigEntryTypes.FLOAT, FloatConfigEntryComponent::new);
        ConfigEntryComponentRegistry.register(ConfigEntryTypes.INTEGER, IntegerConfigEntryComponent::new);
        ConfigEntryComponentRegistry.register(ConfigEntryTypes.LONG, LongConfigEntryComponent::new);
    }
}