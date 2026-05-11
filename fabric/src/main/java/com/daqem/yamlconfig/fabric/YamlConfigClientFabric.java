package com.daqem.yamlconfig.fabric;

import com.daqem.yamlconfig.client.YamlConfigClient;
import com.daqem.yamlconfig.client.event.KeyPressEvent;
import com.daqem.yamlconfig.client.event.PlayerLeaveEvent;
import com.daqem.yamlconfig.client.networking.ClientboundOpenConfigScreenPacketHandler;
import com.daqem.yamlconfig.client.networking.ClientboundOpenConfigsScreenPacketHandler;
import com.daqem.yamlconfig.client.networking.ClientboundSyncConfigPacketHandler;
import com.daqem.yamlconfig.networking.YamlConfigNetworking;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class YamlConfigClientFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        YamlConfigClient.init();
        registerNetworking();
        registerEvents();
    }

    private void registerEvents() {
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) ->
                PlayerLeaveEvent.onPlayerLeave());
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (YamlConfigClient.CONFIGS_KEY.consumeClick()) {
                KeyPressEvent.onKeyPress(client, KeyBindingHelper.getBoundKeyOf(YamlConfigClient.CONFIGS_KEY).getValue(), 0, 1);
            }
        });
    }

    private void registerNetworking() {
        ClientPlayNetworking.registerGlobalReceiver(YamlConfigNetworking.CLIENTBOUND_SYNC_CONFIG, (payload, context) -> ClientboundSyncConfigPacketHandler.handleClientSide(payload));
        ClientPlayNetworking.registerGlobalReceiver(YamlConfigNetworking.CLIENTBOUND_OPEN_CONFIGS_SCREEN_PACKET, (payload, context) -> ClientboundOpenConfigsScreenPacketHandler.handleClientSide(payload));
        ClientPlayNetworking.registerGlobalReceiver(YamlConfigNetworking.CLIENTBOUND_OPEN_CONFIG_SCREEN_PACKET, (payload, context) -> ClientboundOpenConfigScreenPacketHandler.handleClientSide(payload));
    }
}