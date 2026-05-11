package com.daqem.yamlconfig.client.gui.component.entry;

import com.daqem.uilib.gui.component.AbstractComponent;
import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.gui.component.IConfigEntryComponent;
import com.daqem.yamlconfig.client.gui.component.ResetValueButtonComponent;
import com.daqem.yamlconfig.client.gui.component.TruncatedKeyTextComponent;
import net.minecraft.client.gui.GuiGraphics;

public abstract class BaseConfigEntryComponent<C extends IConfigEntry<?>> extends AbstractComponent implements IConfigEntryComponent<C> {

    public static final int KEY_WIDTH = 136;
    public static final int VALUE_WIDTH = 150;
    public static final int RELOAD_WIDTH = 20;
    public static final int GAP_WIDTH = 4;
    public static final int TOTAL_WIDTH = KEY_WIDTH + GAP_WIDTH + VALUE_WIDTH + GAP_WIDTH + RELOAD_WIDTH;
    public static final int DEFAULT_HEIGHT = 20;

    protected final C configEntry;

    protected final TruncatedKeyTextComponent keyText;
    protected final ResetValueButtonComponent resetValueButton;

    public BaseConfigEntryComponent(String key, C configEntry, int x, int y, int height) {
        this(key, configEntry, x, y, height, KEY_WIDTH);
    }

    public BaseConfigEntryComponent(String key, C configEntry, int x, int y, int height, int textWidth) {
        super(x, y, TOTAL_WIDTH, height);
        this.configEntry = configEntry;

        this.keyText = new TruncatedKeyTextComponent(key, textWidth, configEntry, false);
        this.resetValueButton = new ResetValueButtonComponent(TOTAL_WIDTH - RELOAD_WIDTH, 0, button -> resetValue());

        this.addComponent(this.keyText);
        this.addWidget(this.resetValueButton);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, int parentWidth, int parentHeight) {
        this.resetValueButton.active = !isOriginalValue();
    }

    @Override
    public C getConfigEntry() {
        return configEntry;
    }

    public abstract boolean isOriginalValue();

    public abstract void resetValue();

    @Override
    public boolean hasValidationErrors() {
        return false;
    }
}
