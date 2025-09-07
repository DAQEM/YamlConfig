package com.daqem.yamlconfig.client.gui.component.entry.numeric;

import com.daqem.uilib.gui.widget.EditBoxWidget;
import com.daqem.yamlconfig.api.config.entry.numeric.INumericConfigEntry;
import com.daqem.yamlconfig.api.gui.component.IComponentValidator;
import com.daqem.yamlconfig.client.gui.component.entry.BaseConfigEntryComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.util.List;

public abstract class BaseNumericConfigEntryComponent<C extends INumericConfigEntry<N>, N extends Number & Comparable<N>> extends BaseConfigEntryComponent<C> {

    protected final EditBoxWidget editBoxWidget;

    public BaseNumericConfigEntryComponent(String key, C configEntry, IComponentValidator validator) {
        super(key, configEntry, 0, 0, DEFAULT_HEIGHT);

        this.editBoxWidget = new EditBoxWidget(
                Minecraft.getInstance().font,
                KEY_WIDTH + GAP_WIDTH,
                0,
                VALUE_WIDTH,
                DEFAULT_HEIGHT,
                Component.empty()
        ) {
            @Override
            public List<Component> validateInput(String input) {
                return validator.validate(input);
            }
        };

        editBoxWidget.setValue(configEntry.get().toString());
        editBoxWidget.setMaxLength(configEntry.getMaxValue().toString().length());

        this.addWidget(editBoxWidget);
    }

    @Override
    public boolean isOriginalValue() {
        try {
            return this.getConfigEntry().getDefaultValue().toString().equals(this.editBoxWidget.getValue());
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public void resetValue() {
        this.editBoxWidget.setValue(this.getConfigEntry().getDefaultValue().toString());
    }

    @Override
    public boolean hasValidationErrors() {
        return this.editBoxWidget.hasInputValidationErrors();
    }
}
