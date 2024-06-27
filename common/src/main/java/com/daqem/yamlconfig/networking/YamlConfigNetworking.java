package com.daqem.yamlconfig.networking;

import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.networking.s2c.ClientboundSyncConfigPacket;
import dev.architectury.networking.NetworkManager;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public interface YamlConfigNetworking {

    CustomPacketPayload.Type<ClientboundSyncConfigPacket> CLIENTBOUND_SYNC_CONFIG = new CustomPacketPayload.Type<>(YamlConfig.getId("clientbound_sync_config"));

    static void initClient() {
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, CLIENTBOUND_SYNC_CONFIG, ClientboundSyncConfigPacket.STREAM_CODEC, ClientboundSyncConfigPacket::handleClientSide);
    }

    static void initServer() {

    }

    static void init() {
        EnvExecutor.runInEnv(Env.CLIENT, () -> YamlConfigNetworking::initClient);
        EnvExecutor.runInEnv(Env.SERVER, () -> YamlConfigNetworking::initServer);
    }
}
