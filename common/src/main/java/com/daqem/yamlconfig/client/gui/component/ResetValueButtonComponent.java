package com.daqem.yamlconfig.client.gui.component;

import com.daqem.uilib.api.client.gui.component.event.OnClickEvent;
import com.daqem.uilib.client.gui.component.io.ButtonComponent;
import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.client.gui.component.entry.BaseConfigEntryComponent;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class ResetValueButtonComponent extends ButtonComponent {

    public ResetValueButtonComponent(int x, int y, OnClickEvent<ButtonComponent> onClickEvent) {
        super(x, y, BaseConfigEntryComponent.RELOAD_WIDTH, BaseConfigEntryComponent.DEFAULT_HEIGHT, Component.empty(), onClickEvent);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);
        if (!isEnabled()) {
            RenderSystem.setShaderColor(0.8F, 0.8F, 0.8F, 1.0F);
        }
        graphics.blitSprite(YamlConfig.getId("widget/reload"), 3, 3, 20, 14, 14);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public void renderTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        if (isTotalHovered(mouseX, mouseY)) {
            guiGraphics.renderTooltip(Minecraft.getInstance().font, YamlConfig.translatable("gui.tooltip.reset_value"), mouseX, mouseY);
        }
    }
}
