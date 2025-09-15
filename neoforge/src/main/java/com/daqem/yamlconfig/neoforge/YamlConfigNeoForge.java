package com.daqem.yamlconfig.neoforge;

import com.daqem.yamlconfig.YamlConfig;
import net.neoforged.fml.common.Mod;

@Mod(YamlConfig.MOD_ID)
public class YamlConfigNeoForge {

    public YamlConfigNeoForge() {
        YamlConfig.init();
    }
}
