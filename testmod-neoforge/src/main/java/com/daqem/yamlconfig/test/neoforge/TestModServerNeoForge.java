package com.daqem.yamlconfig.test.neoforge;

import com.daqem.yamlconfig.test.TestMod;
import com.daqem.yamlconfig.test.TestModServer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = TestMod.MOD_ID, dist = Dist.DEDICATED_SERVER)
public class TestModServerNeoForge {

    public TestModServerNeoForge() {
        TestModServer.init();
    }
}
