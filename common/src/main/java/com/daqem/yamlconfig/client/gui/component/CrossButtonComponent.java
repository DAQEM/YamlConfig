package com.daqem.yamlconfig.client.gui.component;

import com.daqem.uilib.gui.widget.ButtonWidget;
import com.daqem.yamlconfig.YamlConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;

public class CrossButtonComponent extends ButtonWidget {

    private static final WidgetSprites DEFAULT_SPRITES = new WidgetSprites(
            Identifier.withDefaultNamespace("widget/cross_button"),
            YamlConfig.getId("widget/cross_button_disabled"),
            Identifier.withDefaultNamespace("widget/cross_button_highlighted")
    );

    public CrossButtonComponent(int x, int y, OnPress onPress) {
        super(x, y, 14, 14, Component.empty(), onPress);
    }

    @Override
    protected void renderContents(GuiGraphics guiGraphics, int i, int j, float f) {
        guiGraphics.blitSprite(
                RenderPipelines.GUI_TEXTURED,
                DEFAULT_SPRITES.get(this.active, this.isHoveredOrFocused()),
                this.getX(),
                this.getY(),
                this.getWidth(),
                this.getHeight(),
                ARGB.white(this.alpha)
        );
        this.renderDefaultLabel(guiGraphics.textRendererForWidget(this, GuiGraphics.HoveredTextEffects.NONE));
    }
}
