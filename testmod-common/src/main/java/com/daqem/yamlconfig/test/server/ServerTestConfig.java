package com.daqem.yamlconfig.test.server;

import com.daqem.yamlconfig.api.config.ConfigExtension;
import com.daqem.yamlconfig.api.config.ConfigType;
import com.daqem.yamlconfig.impl.config.ConfigBuilder;
import com.daqem.yamlconfig.test.config.TestConfigPopulator;

public class ServerTestConfig {

    public static void init() {
        createConfig(ConfigExtension.YAML);
        createConfig(ConfigExtension.JSON5);
        createConfig(ConfigExtension.TOML);
        createConfig(ConfigExtension.HOCON);
    }

    private static void createConfig(ConfigExtension extension) {
        String name = "test-server-" + extension.getExtension().replace(".", "");
        ConfigBuilder builder = new ConfigBuilder("yamlconfig_test", name, extension, ConfigType.SERVER);
        TestConfigPopulator.populate(builder);
        builder.build();
    }
}