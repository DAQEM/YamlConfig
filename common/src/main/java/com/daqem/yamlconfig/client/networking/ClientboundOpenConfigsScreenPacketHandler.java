package com.daqem.yamlconfig.client.networking;

import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.api.config.IConfig;
import com.daqem.yamlconfig.client.gui.screen.ConfigsScreen;
import com.daqem.yamlconfig.networking.s2c.ClientboundOpenConfigsScreenPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

import java.util.ArrayList;
import java.util.List;

public class ClientboundOpenConfigsScreenPacketHandler {

    public static void handleClientSide(ClientboundOpenConfigsScreenPacket packet) {
        Minecraft client = Minecraft.getInstance();
        Screen screen = client.screen;

        if (screen instanceof ConfigsScreen configsScreen) {
            configsScreen.addServerConfigs(packet.configs);
        } else {
            List<IConfig> clientConfigs = YamlConfig.CONFIG_MANAGER.getAllClientConfigs();
            for (IConfig clientConfig : clientConfigs) {
                if (packet.configs.containsKey(clientConfig.getModId())) {
                    packet.configs.get(clientConfig.getModId()).add(clientConfig);
                } else {
                    packet.configs.put(clientConfig.getModId(), new ArrayList<>(List.of(clientConfig)));
                }
            }
            client.setScreen(new ConfigsScreen(packet.configs));
        }
    }
}