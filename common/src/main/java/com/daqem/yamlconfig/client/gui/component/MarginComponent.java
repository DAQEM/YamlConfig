package com.daqem.yamlconfig.client.gui.component;

import com.daqem.uilib.client.gui.component.AbstractComponent;
import net.minecraft.client.gui.GuiGraphics;

public class MarginComponent extends AbstractComponent<MarginComponent> {

    public MarginComponent(int width, int height) {
        super(null, 0, 0, width, height);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
    }
}
