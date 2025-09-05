package com.daqem.yamlconfig.client.gui.component;

import com.daqem.uilib.gui.widget.ButtonWidget;
import com.daqem.yamlconfig.YamlConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;

public class CrossButtonComponent extends ButtonWidget {

    private static final WidgetSprites DEFAULT_SPRITES = new WidgetSprites(
            ResourceLocation.withDefaultNamespace("widget/cross_button"),
            YamlConfig.getId("widget/cross_button_disabled"),
            ResourceLocation.withDefaultNamespace("widget/cross_button_highlighted")
    );

    public CrossButtonComponent(int x, int y, OnPress onPress) {
        super(x, y, 14, 14, Component.empty(), onPress);
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
        Minecraft minecraft = Minecraft.getInstance();
        guiGraphics.blitSprite(
                RenderPipelines.GUI_TEXTURED,
                DEFAULT_SPRITES.get(this.active, this.isHoveredOrFocused()),
                this.getX(),
                this.getY(),
                this.getWidth(),
                this.getHeight(),
                ARGB.white(this.alpha)
        );
        int k = ARGB.color(this.alpha, this.active ? -1 : -6250336);
        this.renderString(guiGraphics, minecraft.font, k);
    }
}
