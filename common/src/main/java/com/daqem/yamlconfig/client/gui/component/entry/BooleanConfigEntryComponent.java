package com.daqem.yamlconfig.client.gui.component.entry;

import com.daqem.uilib.gui.widget.CycleButtonWidget;
import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.api.config.entry.IBooleanConfigEntry;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.network.chat.Component;

import java.util.List;

public class BooleanConfigEntryComponent extends BaseConfigEntryComponent<IBooleanConfigEntry> {

    private final CycleButtonWidget<Boolean> cycleButtonComponent;

    public BooleanConfigEntryComponent(String key, IBooleanConfigEntry configEntry) {
        super(key, configEntry, 0, 0, DEFAULT_HEIGHT);

        this.cycleButtonComponent = new CycleButtonWidget<>(
                KEY_WIDTH + GAP_WIDTH,
                0,
                VALUE_WIDTH,
                DEFAULT_HEIGHT,
                Component.empty(),
                Component.empty(),
                configEntry.get() ? 0 : 1,
                configEntry.get() ? Boolean.TRUE : Boolean.FALSE,
                CycleButton.ValueListSupplier.create(List.of(Boolean.TRUE, Boolean.FALSE)),
                value -> value == Boolean.TRUE ? YamlConfig.translatable("gui.value.true") : YamlConfig.translatable("gui.value.false"),
                CycleButton::createDefaultNarrationMessage,
                (cycleButton, aBoolean) -> {
                },
                val -> null,
                true
        );
        this.cycleButtonComponent.setValue(configEntry.get() ? Boolean.TRUE : Boolean.FALSE);
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
