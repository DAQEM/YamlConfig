package com.daqem.yamlconfig.test.client.gui.component.entry;

import com.daqem.uilib.gui.widget.EditBoxWidget;
import com.daqem.yamlconfig.client.gui.component.entry.BaseConfigEntryComponent;
import com.daqem.yamlconfig.test.config.entry.TestConfigEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class TestConfigEntryComponent extends BaseConfigEntryComponent<TestConfigEntry> {

    private final EditBoxWidget editBoxWidget;

    public TestConfigEntryComponent(String key, TestConfigEntry configEntry) {
        super(key, configEntry, 0, 0, DEFAULT_HEIGHT);

        // Custom Test Component Logic: Just a standard edit box for this example,
        // but could be anything (color picker, slider, etc.)
        this.editBoxWidget = new EditBoxWidget(
                Minecraft.getInstance().font,
                KEY_WIDTH + GAP_WIDTH,
                0,
                VALUE_WIDTH,
                DEFAULT_HEIGHT,
                Component.empty()
        );

        editBoxWidget.setValue(configEntry.get());
        this.addWidget(editBoxWidget);
    }

    @Override
    public boolean isOriginalValue() {
        return getConfigEntry().getDefaultValue().equals(editBoxWidget.getValue());
    }

    @Override
    public void resetValue() {
        editBoxWidget.setValue(getConfigEntry().getDefaultValue());
    }

    @Override
    public void applyValue() {
        getConfigEntry().set(editBoxWidget.getValue());
    }
}