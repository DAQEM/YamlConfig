package com.daqem.yamlconfig.neoforge;

import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.client.YamlConfigClient;
import com.daqem.yamlconfig.client.event.KeyPressEvent;
import com.daqem.yamlconfig.client.event.PlayerLeaveEvent;
import com.daqem.yamlconfig.neoforge.platform.PlatformHelperNeoForge;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@Mod(value = YamlConfig.MOD_ID, dist = Dist.CLIENT)
public class YamlConfigNeoForgeClient {

    public YamlConfigNeoForgeClient(IEventBus modEventBus) {
        YamlConfigClient.init();
        modEventBus.addListener(this::registerKeys);
        NeoForge.EVENT_BUS.register(this);
    }

    private void registerKeys(RegisterKeyMappingsEvent event) {
        PlatformHelperNeoForge.KEYS_TO_REGISTER.forEach(event::register);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.Key event) {
        KeyPressEvent.onKeyPress(Minecraft.getInstance(), event.getKey(), event.getScanCode(), event.getAction());
    }

    @SubscribeEvent
    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity().level().isClientSide()) {
            PlayerLeaveEvent.onPlayerLeave();
        }
    }
}