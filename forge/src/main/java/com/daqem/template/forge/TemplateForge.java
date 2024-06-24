package com.daqem.template.neoforge;

import dev.architectury.platform.forge.EventBuses;
import com.daqem.template.Template;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Template.MOD_ID)
public class TemplateForge {
    public TemplateForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(Template.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        Template.init();
    }
}
