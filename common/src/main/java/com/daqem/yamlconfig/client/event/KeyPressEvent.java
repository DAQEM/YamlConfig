package com.daqem.yamlconfig.client.event;

import com.daqem.uilib.api.widget.IInputValidatable;
import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.api.config.IConfig;
import com.daqem.yamlconfig.client.YamlConfigClient;
import com.daqem.yamlconfig.client.gui.screen.ConfigsScreen;
import com.daqem.yamlconfig.networking.c2s.ServerboundOpenConfigsScreenPacket;
import com.daqem.yamlconfig.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyPressEvent {

    public static void onKeyPress(Minecraft client, KeyEvent event, int action) {
        Screen screen = client.screen;
        if (action == 1 && YamlConfigClient.CONFIGS_KEY.matches(event)) {
            if (screen instanceof ConfigsScreen configsScreen && !(configsScreen.getFocused() instanceof IInputValidatable)) {
                screen.onClose();
            } else if (screen == null) {
                Map<String, List<IConfig>> clientConfigsMap = new HashMap<>();
                for (IConfig clientConfig : YamlConfig.CONFIG_MANAGER.getAllClientConfigs()) {
                    clientConfigsMap.computeIfAbsent(clientConfig.getModId(), k -> new ArrayList<>()).add(clientConfig);
                }
                client.setScreen(new ConfigsScreen(clientConfigsMap));

                Services.PLATFORM.sendToServer(new ServerboundOpenConfigsScreenPacket());
            }
        }
    }
}