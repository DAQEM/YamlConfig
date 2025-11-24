package com.daqem.yamlconfig.client.networking;

import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.networking.s2c.ClientboundSyncConfigPacket;

public class ClientboundSyncConfigPacketHandler {

    public static void handleClientSide(ClientboundSyncConfigPacket packet) {
        packet.config.sync(packet.data);
        YamlConfig.LOGGER.info("Received update for config file: {}{}", packet.config.getName(), packet.config.getExtension().getExtension());
    }
}
