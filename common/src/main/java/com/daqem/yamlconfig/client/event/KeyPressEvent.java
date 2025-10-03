package com.daqem.yamlconfig.client.event;

import com.daqem.uilib.api.widget.IInputValidatable;
import com.daqem.yamlconfig.client.YamlConfigClient;
import com.daqem.yamlconfig.client.gui.screen.ConfigsScreen;
import com.daqem.yamlconfig.networking.c2s.ServerboundOpenConfigsScreenPacket;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientRawInputEvent;
import dev.architectury.networking.NetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;

public class KeyPressEvent {

    public static void registerEvent() {
        ClientRawInputEvent.KEY_PRESSED.register((Minecraft client, int keyCode, KeyEvent keyEvent) -> {
            Screen screen = client.screen;
            if (YamlConfigClient.CONFIGS_KEY.matches(keyEvent) && keyCode == 1) {
                if (screen instanceof ConfigsScreen configsScreen && !(configsScreen.getFocused() instanceof IInputValidatable)) screen.onClose();
                else if (screen == null) NetworkManager.sendToServer(new ServerboundOpenConfigsScreenPacket());
            }
            return EventResult.pass();
        });
    }
}
