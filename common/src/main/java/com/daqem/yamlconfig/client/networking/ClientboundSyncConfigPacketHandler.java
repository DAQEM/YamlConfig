package com.daqem.yamlconfig.client.networking;

import com.daqem.yamlconfig.networking.s2c.ClientboundSyncConfigPacket;
import dev.architectury.networking.NetworkManager;

public class ClientboundSyncConfigPacketHandler {

    public static void handleClientSide(ClientboundSyncConfigPacket packet, NetworkManager.PacketContext packetContext) {
        packet.config.sync(packet.data);
    }
}
