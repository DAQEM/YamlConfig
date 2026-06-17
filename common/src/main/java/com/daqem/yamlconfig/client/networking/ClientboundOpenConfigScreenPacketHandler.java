package com.daqem.yamlconfig.client.networking;

import com.daqem.yamlconfig.client.gui.screen.ConfigScreen;
import com.daqem.yamlconfig.networking.s2c.ClientboundOpenConfigScreenPacket;
import net.minecraft.client.Minecraft;

public class ClientboundOpenConfigScreenPacketHandler {

    public static void handleClientSide(ClientboundOpenConfigScreenPacket packet) {
        Minecraft.getInstance().gui.setScreen(new ConfigScreen(Minecraft.getInstance().gui.screen(), packet.config));
    }
}
