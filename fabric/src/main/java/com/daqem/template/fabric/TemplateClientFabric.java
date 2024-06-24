package com.daqem.template.fabric;

import com.daqem.template.client.TemplateClient;
import net.fabricmc.api.ClientModInitializer;

public class TemplateClientFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        TemplateClient.init();
    }
}
