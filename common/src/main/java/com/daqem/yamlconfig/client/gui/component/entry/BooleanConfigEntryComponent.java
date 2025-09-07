package com.daqem.yamlconfig.client.gui.component.entry;

import com.daqem.uilib.gui.widget.CycleButtonWidget;
import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.impl.config.entry.BooleanConfigEntry;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.network.chat.Component;

import java.util.List;

public class BooleanConfigEntryComponent extends BaseConfigEntryComponent<BooleanConfigEntry> {

    private final CycleButtonWidget<Boolean> cycleButtonComponent;

    public BooleanConfigEntryComponent(String key, BooleanConfigEntry configEntry) {
        super(key, configEntry, 0, 0, DEFAULT_HEIGHT);

        this.cycleButtonComponent = new CycleButtonWidget<>(
                KEY_WIDTH + GAP_WIDTH,
                0,
                VALUE_WIDTH,
                DEFAULT_HEIGHT,
                Component.empty(),
                configEntry.get() ? 0 : 1,
                configEntry.get(),
                CycleButton.ValueListSupplier.create(List.of(true, false)),
                value -> value ? YamlConfig.translatable("gui.value.true") : YamlConfig.translatable("gui.value.false"),
                CycleButton::createDefaultNarrationMessage,
                (button, value) -> {
                },
                value -> null,
                true
        );
        this.addWidget(this.cycleButtonComponent);
    }

    @Override
    public boolean isOriginalValue() {
        return this.getConfigEntry().getDefaultValue().equals(this.cycleButtonComponent.getValue());
    }

    @Override
    public void resetValue() {
        this.cycleButtonComponent.setValue(this.getConfigEntry().getDefaultValue());
    }

    @Override
    public void applyValue() {
        this.getConfigEntry().set(this.cycleButtonComponent.getValue());
    }
}
