package com.daqem.yamlconfig.neoforge;

import net.neoforged.fml.loading.FMLPaths;

import java.nio.file.Path;

public class YamlConfigExpectPlatformImpl {
    /**
     * This is our actual method to {@link com.daqem.yamlconfig.YamlConfigExpectPlatform#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }
}
