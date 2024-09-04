package com.daqem.yamlconfig.client.gui.component;

import com.daqem.uilib.client.gui.component.AbstractComponent;
import com.daqem.uilib.client.gui.text.TruncatedText;
import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.client.gui.component.entry.BaseConfigEntryComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class TruncatedKeyTextComponent extends AbstractComponent<TruncatedKeyTextComponent> {

    private final String key;

    public TruncatedKeyTextComponent(String key, int width, int height) {
        super(null, 0, 5, width, height);
        this.key = key;
        setText(new TruncatedText(Minecraft.getInstance().font, YamlConfig.translatable(key), 0, 0, width, height));
    }

    @Override
    public void startRenderable() {
        super.startRenderable();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
    }

    @Override
    public void renderTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        if (isTotalHovered(mouseX, mouseY)) {
            if (this.getText() instanceof TruncatedText truncatedText) {
                String key = truncatedText.getText().getString().replace(truncatedText.getEnding(), "");
                if (this.key.contains(key)) {
                    guiGraphics.renderTooltip(this.getText().getFont(), YamlConfig.translatable(this.key), mouseX, mouseY);
                }
            }
        }
    }
}
