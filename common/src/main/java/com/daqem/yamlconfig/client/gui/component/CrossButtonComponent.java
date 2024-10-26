package com.daqem.yamlconfig.client.gui.component;

import com.daqem.uilib.api.client.gui.component.event.OnClickEvent;
import com.daqem.uilib.client.gui.component.io.ButtonComponent;
import com.daqem.yamlconfig.YamlConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.LinkedList;
import java.util.List;

public class CrossButtonComponent extends ButtonComponent {

    private static final LinkedList<ResourceLocation> DEFAULT_SPRITES = new LinkedList<>(List.of(
            ResourceLocation.withDefaultNamespace("widget/cross_button"),
            YamlConfig.getId("widget/cross_button_disabled"),
            ResourceLocation.withDefaultNamespace("widget/cross_button_highlighted")
    ));

    public CrossButtonComponent(int x, int y, OnClickEvent<ButtonComponent> onClickEvent) {
        super(DEFAULT_SPRITES, x, y, 14, 14, Component.empty(), onClickEvent);
    }
}
