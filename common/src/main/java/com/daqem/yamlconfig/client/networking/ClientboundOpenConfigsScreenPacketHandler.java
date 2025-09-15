package com.daqem.yamlconfig.client.networking;

import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.api.config.IConfig;
import com.daqem.yamlconfig.client.gui.screen.ConfigsScreen;
import com.daqem.yamlconfig.networking.s2c.ClientboundOpenConfigsScreenPacket;
import dev.architectury.networking.NetworkManager;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;

public class ClientboundOpenConfigsScreenPacketHandler {

    public static void handleClientSide(ClientboundOpenConfigsScreenPacket packet, NetworkManager.PacketContext packetContext) {
        List<IConfig> clientConfigs = YamlConfig.CONFIG_MANAGER.getAllClientConfigs();

        for (IConfig clientConfig : clientConfigs) {
            if (packet.configs.containsKey(clientConfig.getModId())) {
                packet.configs.get(clientConfig.getModId()).add(clientConfig);
            } else {
                packet.configs.put(clientConfig.getModId(), new ArrayList<>(List.of(clientConfig)));
            }
        }
        Minecraft.getInstance().setScreen(new ConfigsScreen(packet.configs));
    }
}
