package com.daqem.yamlconfig.test.neoforge;

import com.daqem.yamlconfig.test.TestMod;
import net.neoforged.fml.common.Mod;

@Mod(TestMod.MOD_ID)
public class TestModNeoForge {
    public TestModNeoForge() {
        TestMod.init();
    }
}
