package com.daqem.yamlconfig;

import com.daqem.yamlconfig.test.TestConfig;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

public class YamlConfig {
    public static final String MOD_ID = "yamlconfig";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static void init() {
        TestConfig.init();
    }
}
