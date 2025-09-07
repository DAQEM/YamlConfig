package com.daqem.yamlconfig.client.gui.component.entry;

import com.daqem.uilib.gui.widget.CycleButtonWidget;
import com.daqem.yamlconfig.impl.config.entry.EnumConfigEntry;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.List;

public class EnumConfigEntryComponent<E extends Enum<E>> extends BaseConfigEntryComponent<EnumConfigEntry<E>>{

    private final CycleButtonWidget<E> cycleButtonComponent;

    public EnumConfigEntryComponent(String key, EnumConfigEntry<E> configEntry) {
        super(key, configEntry, 0, 0, DEFAULT_HEIGHT);

        List<E> values = Arrays.asList(configEntry.getEnumClass().getEnumConstants());
        E value = configEntry.get();
        int index = values.indexOf(value);

        this.cycleButtonComponent = new CycleButtonWidget<>(
                KEY_WIDTH + GAP_WIDTH,
                0,
                VALUE_WIDTH,
                DEFAULT_HEIGHT,
                Component.empty(),
                index,
                value,
                CycleButton.ValueListSupplier.create(values),
                e -> Component.literal(e.name()),
                CycleButton::createDefaultNarrationMessage,
                (button, e) -> {
                },
                e -> null,
                false
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
