package com.daqem.yamlconfig.networking;

import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.networking.c2s.ServerboundOpenConfigScreenPacket;
import com.daqem.yamlconfig.networking.c2s.ServerboundOpenConfigsScreenPacket;
import com.daqem.yamlconfig.networking.c2s.ServerboundSaveConfigPacket;
import com.daqem.yamlconfig.networking.s2c.ClientboundOpenConfigScreenPacket;
import com.daqem.yamlconfig.networking.s2c.ClientboundOpenConfigsScreenPacket;
import com.daqem.yamlconfig.networking.s2c.ClientboundSyncConfigPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public interface YamlConfigNetworking {

    CustomPacketPayload.Type<ClientboundSyncConfigPacket> CLIENTBOUND_SYNC_CONFIG = new CustomPacketPayload.Type<>(YamlConfig.getId("clientbound_sync_config"));
    CustomPacketPayload.Type<ClientboundOpenConfigsScreenPacket> CLIENTBOUND_OPEN_CONFIGS_SCREEN_PACKET = new CustomPacketPayload.Type<>(YamlConfig.getId("clientbound_open_configs_screen_packet"));
    CustomPacketPayload.Type<ClientboundOpenConfigScreenPacket> CLIENTBOUND_OPEN_CONFIG_SCREEN_PACKET = new CustomPacketPayload.Type<>(YamlConfig.getId("clientbound_open_config_screen_packet"));

    CustomPacketPayload.Type<ServerboundOpenConfigsScreenPacket> SERVERBOUND_OPEN_CONFIGS_SCREEN_PACKET = new CustomPacketPayload.Type<>(YamlConfig.getId("serverbound_open_configs_screen_packet"));
    CustomPacketPayload.Type<ServerboundOpenConfigScreenPacket> SERVERBOUND_OPEN_CONFIG_SCREEN_PACKET = new CustomPacketPayload.Type<>(YamlConfig.getId("serverbound_open_config_screen_packet"));
    CustomPacketPayload.Type<ServerboundSaveConfigPacket> SERVERBOUND_SAVE_CONFIG_PACKET = new CustomPacketPayload.Type<>(YamlConfig.getId("serverbound_save_config_packet"));
}