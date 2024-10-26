package com.daqem.yamlconfig.client.gui.component.entry;

import com.daqem.uilib.client.gui.component.io.CycleButtonComponent;
import com.daqem.uilib.client.gui.component.io.IOComponentEntry;
import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.impl.config.entry.BooleanConfigEntry;
import net.minecraft.network.chat.Component;

import java.util.Arrays;

public class BooleanConfigEntryComponent extends BaseConfigEntryComponent<BooleanConfigEntryComponent, BooleanConfigEntry> {

    private final CycleButtonComponent<Boolean> cycleButtonComponent;

    public BooleanConfigEntryComponent(String key, BooleanConfigEntry configEntry) {
        super(key, configEntry, 0, 0, DEFAULT_HEIGHT);

        this.cycleButtonComponent = new CycleButtonComponent<>(KEY_WIDTH + GAP_WIDTH, 0, VALUE_WIDTH, DEFAULT_HEIGHT,
                Arrays.asList(
                        new IOComponentEntry<>(Boolean.toString(true), YamlConfig.translatable("gui.value.true"), true),
                        new IOComponentEntry<>(Boolean.toString(false), YamlConfig.translatable("gui.value.false"), false)
                ),
                configEntry.get(),
                Component.empty());
    }

    @Override
    public void startRenderable() {
        this.addChild(this.cycleButtonComponent);
        super.startRenderable();
    }

    @Override
    public boolean isOriginalValue() {
        return this.getConfigEntry().get().equals(this.cycleButtonComponent.getValue());
    }

    @Override
    public void resetValue() {
        this.cycleButtonComponent.setValue(this.getConfigEntry().get());
    }

    @Override
    public void applyValue() {
        this.getConfigEntry().set(this.cycleButtonComponent.getValue());
    }
}
