package com.daqem.yamlconfig.fabric;

import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class YamlConfigExpectPlatformImpl {
    /**
     * This is our actual method to {@link com.daqem.yamlconfig.YamlConfigExpectPlatform#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
