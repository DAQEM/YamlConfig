package com.daqem.yamlconfig.client.networking;

import com.daqem.yamlconfig.client.gui.screen.ConfigScreen;
import com.daqem.yamlconfig.networking.s2c.ClientboundOpenConfigScreenPacket;
import dev.architectury.networking.NetworkManager;
import net.minecraft.client.Minecraft;

public class ClientboundOpenConfigScreenPacketHandler {

    public static void handleClientSide(ClientboundOpenConfigScreenPacket packet, NetworkManager.PacketContext packetContext) {
        Minecraft.getInstance().setScreen(new ConfigScreen(Minecraft.getInstance().screen, packet.config));
    }
}
