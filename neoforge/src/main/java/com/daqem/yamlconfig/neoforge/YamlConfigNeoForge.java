package com.daqem.yamlconfig.neoforge;

import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.client.networking.ClientboundOpenConfigScreenPacketHandler;
import com.daqem.yamlconfig.client.networking.ClientboundOpenConfigsScreenPacketHandler;
import com.daqem.yamlconfig.client.networking.ClientboundSyncConfigPacketHandler;
import com.daqem.yamlconfig.event.PlayerJoinEvent;
import com.daqem.yamlconfig.networking.YamlConfigNetworking;
import com.daqem.yamlconfig.networking.c2s.ServerboundOpenConfigScreenPacket;
import com.daqem.yamlconfig.networking.c2s.ServerboundOpenConfigsScreenPacket;
import com.daqem.yamlconfig.networking.c2s.ServerboundSaveConfigPacket;
import com.daqem.yamlconfig.networking.s2c.ClientboundOpenConfigScreenPacket;
import com.daqem.yamlconfig.networking.s2c.ClientboundOpenConfigsScreenPacket;
import com.daqem.yamlconfig.networking.s2c.ClientboundSyncConfigPacket;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@Mod(YamlConfig.MOD_ID)
public class YamlConfigNeoForge {

    public YamlConfigNeoForge(IEventBus modEventBus) {
        YamlConfig.init();
        modEventBus.addListener(this::registerNetworking);
        NeoForge.EVENT_BUS.register(this);
    }

    private void registerNetworking(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");

        // C2S
        registrar.playToServer(YamlConfigNetworking.SERVERBOUND_OPEN_CONFIGS_SCREEN_PACKET, ServerboundOpenConfigsScreenPacket.STREAM_CODEC, (payload, context) ->
                payload.handleServerSide((ServerPlayer) context.player()));
        registrar.playToServer(YamlConfigNetworking.SERVERBOUND_OPEN_CONFIG_SCREEN_PACKET, ServerboundOpenConfigScreenPacket.STREAM_CODEC, (payload, context) ->
                payload.handleServerSide((ServerPlayer) context.player()));
        registrar.playToServer(YamlConfigNetworking.SERVERBOUND_SAVE_CONFIG_PACKET, ServerboundSaveConfigPacket.STREAM_CODEC, (payload, context) ->
                payload.handleServerSide((ServerPlayer) context.player()));

        // S2C
        registrar.playToClient(YamlConfigNetworking.CLIENTBOUND_SYNC_CONFIG, ClientboundSyncConfigPacket.STREAM_CODEC, (payload, context) ->
                ClientboundSyncConfigPacketHandler.handleClientSide(payload));
        registrar.playToClient(YamlConfigNetworking.CLIENTBOUND_OPEN_CONFIGS_SCREEN_PACKET, ClientboundOpenConfigsScreenPacket.STREAM_CODEC, (payload, context) ->
                ClientboundOpenConfigsScreenPacketHandler.handleClientSide(payload));
        registrar.playToClient(YamlConfigNetworking.CLIENTBOUND_OPEN_CONFIG_SCREEN_PACKET, ClientboundOpenConfigScreenPacket.STREAM_CODEC, (payload, context) ->
                ClientboundOpenConfigScreenPacketHandler.handleClientSide(payload));
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            PlayerJoinEvent.onPlayerJoin(serverPlayer);
        }
    }
}