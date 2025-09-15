package com.daqem.yamlconfig.neoforge;

import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.client.YamlConfigClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = YamlConfig.MOD_ID, dist = Dist.CLIENT)
public class YamlConfigNeoForgeClient {

    public YamlConfigNeoForgeClient() {
        YamlConfigClient.init();
    }
}
