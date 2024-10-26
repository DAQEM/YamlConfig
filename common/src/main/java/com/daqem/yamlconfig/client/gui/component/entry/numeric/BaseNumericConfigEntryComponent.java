package com.daqem.yamlconfig.client.gui.component.entry.numeric;

import com.daqem.uilib.client.gui.component.io.TextBoxComponent;
import com.daqem.yamlconfig.api.config.entry.numeric.INumericConfigEntry;
import com.daqem.yamlconfig.api.gui.component.IComponentValidator;
import com.daqem.yamlconfig.client.gui.component.entry.BaseConfigEntryComponent;
import net.minecraft.network.chat.Component;

import java.util.List;

public abstract class BaseNumericConfigEntryComponent<T extends BaseNumericConfigEntryComponent<T, C, N>, C extends INumericConfigEntry<N>, N extends Number & Comparable<N>> extends BaseConfigEntryComponent<T, C> {

    protected final TextBoxComponent textBoxComponent;

    public BaseNumericConfigEntryComponent(String key, C configEntry, N defaultValue, IComponentValidator validator) {
        super(key, configEntry, 0, 0, DEFAULT_HEIGHT);

        this.textBoxComponent = new TextBoxComponent(KEY_WIDTH + GAP_WIDTH, 0, VALUE_WIDTH, DEFAULT_HEIGHT, defaultValue.toString()) {
            @Override
            public List<Component> validateInput(String input) {
                return validator.validate(input);
            }
        };

        textBoxComponent.setMaxLength(configEntry.getMaxValue().toString().length());
    }

    @Override
    public void startRenderable() {
        this.addChild(this.textBoxComponent);
        super.startRenderable();
    }

    @Override
    public boolean isOriginalValue() {
        try {
            return this.getConfigEntry().get().toString().equals(this.textBoxComponent.getValue());
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public void resetValue() {
        this.textBoxComponent.setValue(this.getConfigEntry().get().toString());
    }
}
