package com.daqem.yamlconfig.test.neoforge;

import com.daqem.yamlconfig.test.TestMod;
import com.daqem.yamlconfig.test.TestModClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = TestMod.MOD_ID, dist = Dist.CLIENT)
public class TestModClientNeoForge {

    public TestModClientNeoForge() {
        TestModClient.init();
    }
}
