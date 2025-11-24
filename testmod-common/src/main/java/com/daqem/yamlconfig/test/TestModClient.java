package com.daqem.yamlconfig.test;

import com.daqem.yamlconfig.client.gui.registry.ConfigEntryComponentRegistry;
import com.daqem.yamlconfig.test.client.ClientTestConfig;
import com.daqem.yamlconfig.test.client.gui.component.entry.TestConfigEntryComponent;

public class TestModClient {

    public static void init() {
        // Register the custom UI component for the test entry type
        ConfigEntryComponentRegistry.register(TestMod.TEST_ENTRY_TYPE, TestConfigEntryComponent::new);

        ClientTestConfig.init();
    }
}