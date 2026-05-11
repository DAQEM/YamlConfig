package com.daqem.yamlconfig.client.event;

import com.daqem.uilib.api.widget.IInputValidatable;
import com.daqem.yamlconfig.client.YamlConfigClient;
import com.daqem.yamlconfig.client.gui.screen.ConfigsScreen;
import com.daqem.yamlconfig.networking.c2s.ServerboundOpenConfigsScreenPacket;
import com.daqem.yamlconfig.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

public class KeyPressEvent {

    public static void onKeyPress(Minecraft client, int key, int scanCode, int action) {
        Screen screen = client.screen;
        if (action == 1 && YamlConfigClient.CONFIGS_KEY.matches(key, scanCode)) {
            if (screen instanceof ConfigsScreen configsScreen && !(configsScreen.getFocused() instanceof IInputValidatable)) {
                screen.onClose();
            } else if (screen == null) {
                Services.PLATFORM.sendToServer(new ServerboundOpenConfigsScreenPacket());
            }
        }
    }
}