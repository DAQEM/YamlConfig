package com.daqem.yamlconfig.client.gui.component;

import com.daqem.uilib.gui.widget.CustomButtonWidget;
import com.daqem.yamlconfig.YamlConfig;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class CrossButtonComponent extends CustomButtonWidget {

    private static final WidgetSprites DEFAULT_SPRITES = new WidgetSprites(
            ResourceLocation.withDefaultNamespace("widget/cross_button"),
            YamlConfig.getId("widget/cross_button_disabled"),
            ResourceLocation.withDefaultNamespace("widget/cross_button_highlighted")
    );

    public CrossButtonComponent(int x, int y, OnPress onPress) {
        super(x, y, 14, 14, Component.empty(), DEFAULT_SPRITES, onPress);
    }
}
