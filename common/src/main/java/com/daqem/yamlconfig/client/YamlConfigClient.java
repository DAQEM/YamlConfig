package com.daqem.yamlconfig.client;

import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.YamlConfigExpectPlatform;
import com.daqem.yamlconfig.client.gui.component.entry.*;
import com.daqem.yamlconfig.client.gui.component.entry.list.*;
import com.daqem.yamlconfig.client.gui.component.entry.list.numeric.*;
import com.daqem.yamlconfig.client.gui.component.entry.map.*;
import com.daqem.yamlconfig.client.gui.component.entry.map.numeric.*;
import com.daqem.yamlconfig.client.gui.component.entry.minecraft.*;
import com.daqem.yamlconfig.client.gui.component.entry.numeric.*;
import com.daqem.yamlconfig.client.gui.registry.ConfigEntryComponentRegistry;
import com.daqem.yamlconfig.impl.config.entry.type.ConfigEntryTypes;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;

public class YamlConfigClient {
    public static final KeyMapping.Category YAML_CONFIG_CATEGORY = new KeyMapping.Category(YamlConfig.getId("category"));
    public static final KeyMapping CONFIGS_KEY = new KeyMapping("key.yamlconfig.configs", InputConstants.Type.KEYSYM, InputConstants.KEY_F12, YAML_CONFIG_CATEGORY);

    public static void init() {
        YamlConfigExpectPlatform.registerKeyBinding(CONFIGS_KEY);
        registerComponents();
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