package com.daqem.yamlconfig.client.gui.component.entry;

import com.daqem.uilib.gui.component.text.TextComponent;
import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import net.minecraft.ChatFormatting;

public class YamlOnlyConfigEntryComponent extends BaseConfigEntryComponent<IConfigEntry<?>> {

    public YamlOnlyConfigEntryComponent(String key, IConfigEntry<?> configEntry) {
        super(key, configEntry, 0, 0, DEFAULT_HEIGHT);

        // Hide the reset button as there is nothing to edit/reset here
        this.resetValueButton.visible = false;
        this.resetValueButton.active = false;

        TextComponent label = new TextComponent(
                KEY_WIDTH + GAP_WIDTH,
                6,
                YamlConfig.translatable("gui.yaml_only").withStyle(ChatFormatting.GRAY)
        );
        this.addComponent(label);
    }

    @Override
    public boolean isOriginalValue() {
        return true;
    }

    @Override
    public void resetValue() {
    }

    @Override
    public void applyValue() {
    }
}