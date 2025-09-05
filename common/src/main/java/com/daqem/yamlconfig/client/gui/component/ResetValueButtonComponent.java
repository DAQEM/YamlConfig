package com.daqem.yamlconfig.client.gui.component;

import com.daqem.uilib.gui.widget.ButtonWidget;
import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.client.gui.component.entry.BaseConfigEntryComponent;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;

public class ResetValueButtonComponent extends ButtonWidget {

    public ResetValueButtonComponent(int x, int y, OnPress onPress) {
        super(x, y, BaseConfigEntryComponent.RELOAD_WIDTH, BaseConfigEntryComponent.DEFAULT_HEIGHT, Component.empty(), onPress);
        setTooltip(Tooltip.create(YamlConfig.translatable("gui.tooltip.reset_value")));
    }
}
