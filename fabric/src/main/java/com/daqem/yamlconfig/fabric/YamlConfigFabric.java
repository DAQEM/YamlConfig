package com.daqem.yamlconfig.fabric;

import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.event.PlayerJoinEvent;
import com.daqem.yamlconfig.networking.YamlConfigNetworking;
import com.daqem.yamlconfig.networking.c2s.*;
import com.daqem.yamlconfig.networking.s2c.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class YamlConfigFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        YamlConfig.init();
        registerNetworking();
        registerEvents();
    }

    private void registerEvents() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
                PlayerJoinEvent.onPlayerJoin(handler.player));
    }

    private void registerNetworking() {
        // C2S
        PayloadTypeRegistry.serverboundPlay().register(YamlConfigNetworking.SERVERBOUND_OPEN_CONFIGS_SCREEN_PACKET, ServerboundOpenConfigsScreenPacket.STREAM_CODEC);
        PayloadTypeRegistry.serverboundPlay().register(YamlConfigNetworking.SERVERBOUND_OPEN_CONFIG_SCREEN_PACKET, ServerboundOpenConfigScreenPacket.STREAM_CODEC);
        PayloadTypeRegistry.serverboundPlay().register(YamlConfigNetworking.SERVERBOUND_SAVE_CONFIG_PACKET, ServerboundSaveConfigPacket.STREAM_CODEC);

        ServerPlayNetworking.registerGlobalReceiver(YamlConfigNetworking.SERVERBOUND_OPEN_CONFIGS_SCREEN_PACKET, (payload, context) -> payload.handleServerSide(context.player()));
        ServerPlayNetworking.registerGlobalReceiver(YamlConfigNetworking.SERVERBOUND_OPEN_CONFIG_SCREEN_PACKET, (payload, context) -> payload.handleServerSide(context.player()));
        ServerPlayNetworking.registerGlobalReceiver(YamlConfigNetworking.SERVERBOUND_SAVE_CONFIG_PACKET, (payload, context) -> payload.handleServerSide(context.player()));

        // S2C
        PayloadTypeRegistry.clientboundPlay().register(YamlConfigNetworking.CLIENTBOUND_SYNC_CONFIG, ClientboundSyncConfigPacket.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(YamlConfigNetworking.CLIENTBOUND_OPEN_CONFIGS_SCREEN_PACKET, ClientboundOpenConfigsScreenPacket.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(YamlConfigNetworking.CLIENTBOUND_OPEN_CONFIG_SCREEN_PACKET, ClientboundOpenConfigScreenPacket.STREAM_CODEC);
    }
}