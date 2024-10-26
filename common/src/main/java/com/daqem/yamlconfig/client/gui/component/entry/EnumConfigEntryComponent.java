package com.daqem.yamlconfig.client.gui.component.entry;

import com.daqem.uilib.client.gui.component.io.CycleButtonComponent;
import com.daqem.uilib.client.gui.component.io.IOComponentEntry;
import com.daqem.yamlconfig.impl.config.entry.EnumConfigEntry;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

public class EnumConfigEntryComponent<E extends Enum<E>> extends BaseConfigEntryComponent<EnumConfigEntryComponent<E>, EnumConfigEntry<E>>{

    private final CycleButtonComponent<E> cycleButtonComponent;

    public EnumConfigEntryComponent(String key, EnumConfigEntry<E> configEntry) {
        super(key, configEntry, 0, 0, DEFAULT_HEIGHT);

        this.cycleButtonComponent = new CycleButtonComponent<E>(KEY_WIDTH + GAP_WIDTH, 0, VALUE_WIDTH, DEFAULT_HEIGHT,
                Arrays.stream(configEntry.getEnumClass().getEnumConstants())
                        .map(difficulty -> new IOComponentEntry<>(difficulty.name(), Component.literal(difficulty.name()), difficulty))
                        .collect(Collectors.toList()),
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
        this.getConfigEntry().setValue(this.cycleButtonComponent.getValue());
    }
}
