package com.daqem.yamlconfig.client.gui.component.entry;

import com.daqem.uilib.client.gui.component.AbstractComponent;
import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.gui.component.IConfigEntryComponent;
import com.daqem.yamlconfig.client.gui.component.ResetValueButtonComponent;
import com.daqem.yamlconfig.client.gui.component.TruncatedKeyTextComponent;
import net.minecraft.client.gui.GuiGraphics;

public abstract class BaseConfigEntryComponent<T extends BaseConfigEntryComponent<T, C>, C extends IConfigEntry<?>> extends AbstractComponent<T> implements IConfigEntryComponent<T, C> {

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
        super(null, x, y, TOTAL_WIDTH, height);
        this.configEntry = configEntry;

        this.keyText = new TruncatedKeyTextComponent(key, textWidth, DEFAULT_HEIGHT);
        this.resetValueButton = new ResetValueButtonComponent(TOTAL_WIDTH - RELOAD_WIDTH, 0, (clickedObject, screen, mouseX, mouseY, button) -> {
            resetValue();
            return true;
        });
    }

    @Override
    public void startRenderable() {
        this.addChildren(this.keyText, this.resetValueButton);
        super.startRenderable();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        this.resetValueButton.setEnabled(!isOriginalValue());
    }

    @Override
    public C getConfigEntry() {
        return configEntry;
    }

    public abstract boolean isOriginalValue();

    public abstract void resetValue();

//    public abstract C createConfigEntryCopy();
}
