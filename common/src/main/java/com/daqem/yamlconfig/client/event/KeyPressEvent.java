package com.daqem.yamlconfig.client.event;

import com.daqem.yamlconfig.client.YamlConfigClient;
import com.daqem.yamlconfig.client.gui.screen.ConfigsScreen;
import com.daqem.yamlconfig.networking.c2s.ServerboundOpenConfigsScreenPacket;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientRawInputEvent;
import dev.architectury.networking.NetworkManager;
import net.minecraft.client.gui.screens.Screen;

public class KeyPressEvent {

    public static void registerEvent() {
        ClientRawInputEvent.KEY_PRESSED.register((client, keyCode, scanCode, action, modifiers) -> {
            Screen screen = client.screen;
            if (YamlConfigClient.CONFIGS_KEY.matches(keyCode, scanCode) && action == 1) {
                if (screen instanceof ConfigsScreen) screen.onClose();
                else if (screen == null) NetworkManager.sendToServer(new ServerboundOpenConfigsScreenPacket());
            }
            return EventResult.pass();
        });
    }
}
