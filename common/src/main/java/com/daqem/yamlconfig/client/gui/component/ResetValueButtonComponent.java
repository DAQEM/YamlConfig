package com.daqem.yamlconfig.client.gui.component;

import com.daqem.uilib.gui.widget.ButtonWidget;
import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.client.gui.component.entry.BaseConfigEntryComponent;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;

public class ResetValueButtonComponent extends ButtonWidget {

    public ResetValueButtonComponent(int x, int y, OnPress onPress) {
        super(x, y, BaseConfigEntryComponent.RELOAD_WIDTH, BaseConfigEntryComponent.DEFAULT_HEIGHT, Component.empty(), onPress);
        setTooltip(Tooltip.create(YamlConfig.translatable("gui.tooltip.reset_value")));
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
        super.renderWidget(guiGraphics, i, j, f);
        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().scale(0.6f, 0.6f);
        guiGraphics.pose().translate(getX() * (1.0f / 0.6f - 1) + 6f, getY() * (1.0f / 0.6f - 1) + 6f);
        guiGraphics.blitSprite(
                RenderPipelines.GUI_TEXTURED,
                YamlConfig.getId("widget/reload"),
                getX(),
                getY(),
                getWidth(),
                getHeight()
        );
        guiGraphics.pose().popMatrix();
    }
}
