package com.daqem.template.fabric;

import com.daqem.template.Template;
import net.fabricmc.api.ModInitializer;

public class TemplateCommonFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        Template.init();
    }
}
