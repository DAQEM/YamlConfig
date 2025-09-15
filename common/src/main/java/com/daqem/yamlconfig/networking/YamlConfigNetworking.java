package com.daqem.yamlconfig.networking;

import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.networking.c2s.ServerboundOpenConfigScreenPacket;
import com.daqem.yamlconfig.networking.c2s.ServerboundOpenConfigsScreenPacket;
import com.daqem.yamlconfig.networking.c2s.ServerboundSaveConfigPacket;
import com.daqem.yamlconfig.networking.s2c.ClientboundOpenConfigScreenPacket;
import com.daqem.yamlconfig.networking.s2c.ClientboundOpenConfigsScreenPacket;
import com.daqem.yamlconfig.networking.s2c.ClientboundSyncConfigPacket;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public interface YamlConfigNetworking {

    CustomPacketPayload.Type<ClientboundSyncConfigPacket> CLIENTBOUND_SYNC_CONFIG = new CustomPacketPayload.Type<>(YamlConfig.getId("clientbound_sync_config"));
    CustomPacketPayload.Type<ClientboundOpenConfigsScreenPacket> CLIENTBOUND_OPEN_CONFIGS_SCREEN_PACKET = new CustomPacketPayload.Type<>(YamlConfig.getId("clientbound_open_configs_screen_packet"));
    CustomPacketPayload.Type<ClientboundOpenConfigScreenPacket> CLIENTBOUND_OPEN_CONFIG_SCREEN_PACKET = new CustomPacketPayload.Type<>(YamlConfig.getId("clientbound_open_config_screen_packet"));

    CustomPacketPayload.Type<ServerboundOpenConfigsScreenPacket> SERVERBOUND_OPEN_CONFIGS_SCREEN_PACKET = new CustomPacketPayload.Type<>(YamlConfig.getId("serverbound_open_configs_screen_packet"));
    CustomPacketPayload.Type<ServerboundOpenConfigScreenPacket> SERVERBOUND_OPEN_CONFIG_SCREEN_PACKET = new CustomPacketPayload.Type<>(YamlConfig.getId("serverbound_open_config_screen_packet"));
    CustomPacketPayload.Type<ServerboundSaveConfigPacket> SERVERBOUND_SAVE_CONFIG_PACKET = new CustomPacketPayload.Type<>(YamlConfig.getId("serverbound_save_config_packet"));

    static void initClient() {
        // Register client-side packet receivers
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, CLIENTBOUND_SYNC_CONFIG, ClientboundSyncConfigPacket.STREAM_CODEC, ClientboundSyncConfigPacket::handleClientSide);
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, CLIENTBOUND_OPEN_CONFIGS_SCREEN_PACKET, ClientboundOpenConfigsScreenPacket.STREAM_CODEC, ClientboundOpenConfigsScreenPacket::handleClientSide);
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, CLIENTBOUND_OPEN_CONFIG_SCREEN_PACKET, ClientboundOpenConfigScreenPacket.STREAM_CODEC, ClientboundOpenConfigScreenPacket::handleClientSide);
    }

    static void initServer() {
        // Register server-side packet types and receivers
        NetworkManager.registerS2CPayloadType(CLIENTBOUND_SYNC_CONFIG, ClientboundSyncConfigPacket.STREAM_CODEC);
        NetworkManager.registerS2CPayloadType(CLIENTBOUND_OPEN_CONFIGS_SCREEN_PACKET, ClientboundOpenConfigsScreenPacket.STREAM_CODEC);
        NetworkManager.registerS2CPayloadType(CLIENTBOUND_OPEN_CONFIG_SCREEN_PACKET, ClientboundOpenConfigScreenPacket.STREAM_CODEC);
        
        // Register server-side (C2S) receivers
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, SERVERBOUND_OPEN_CONFIGS_SCREEN_PACKET, ServerboundOpenConfigsScreenPacket.STREAM_CODEC, ServerboundOpenConfigsScreenPacket::handleServerSide);
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, SERVERBOUND_OPEN_CONFIG_SCREEN_PACKET, ServerboundOpenConfigScreenPacket.STREAM_CODEC, ServerboundOpenConfigScreenPacket::handleServerSide);
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, SERVERBOUND_SAVE_CONFIG_PACKET, ServerboundSaveConfigPacket.STREAM_CODEC, ServerboundSaveConfigPacket::handleServerSide);
    }
    
    /**
     * Initialize only server-safe networking components that don't reference client classes
     */
    static void initServerSafe() {
        // Register only server-bound (C2S) packets that are safe to load on dedicated servers
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, SERVERBOUND_OPEN_CONFIGS_SCREEN_PACKET, ServerboundOpenConfigsScreenPacket.STREAM_CODEC, ServerboundOpenConfigsScreenPacket::handleServerSide);
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, SERVERBOUND_OPEN_CONFIG_SCREEN_PACKET, ServerboundOpenConfigScreenPacket.STREAM_CODEC, ServerboundOpenConfigScreenPacket::handleServerSide);
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, SERVERBOUND_SAVE_CONFIG_PACKET, ServerboundSaveConfigPacket.STREAM_CODEC, ServerboundSaveConfigPacket::handleServerSide);
    }

    static void init() {
        // This method was causing issues on dedicated servers by loading client-side classes
        // Now initialization is handled separately for client and server
    }
}
