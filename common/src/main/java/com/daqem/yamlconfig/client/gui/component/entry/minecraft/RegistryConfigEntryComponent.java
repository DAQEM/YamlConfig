package com.daqem.yamlconfig.client.gui.component.entry.minecraft;

import com.daqem.uilib.gui.widget.EditBoxWidget;
import com.daqem.uilib.util.ValidationErrors;
import com.daqem.yamlconfig.api.config.entry.minecraft.IRegistryConfigEntry;
import com.daqem.yamlconfig.client.gui.component.entry.BaseConfigEntryComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.Optional;

public class RegistryConfigEntryComponent<T> extends BaseConfigEntryComponent<IRegistryConfigEntry<T>> {

    private final EditBoxWidget editBoxWidget;

    public RegistryConfigEntryComponent(String key, IRegistryConfigEntry<T> configEntry) {
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
                List<Component> list = super.validateInput(input);
                Identifier value = Identifier.tryParse(input);
                if (value == null || value.getPath().isEmpty() || value.getNamespace().isEmpty() || value.getPath().contains(" ") || value.getNamespace().contains(" ")) {
                    list.add(ValidationErrors.invalidIdentifier());
                } else {
                    if (!getConfigEntry().getRegistry().keySet().contains(value)) {
                        list.add(ValidationErrors.invalidRegistryValue());
                    }
                }
                return list;
            }
        };

        Identifier resourceLocation = getConfigEntry().getRegistry().getKey(getConfigEntry().get());
        String stringValue = resourceLocation != null ? resourceLocation.toString() : "unknown";

        editBoxWidget.setValue(stringValue);
        editBoxWidget.setMaxLength(256);

        this.addWidget(editBoxWidget);
    }

    @Override
    public boolean isOriginalValue() {
        T defaultValue = getConfigEntry().getDefaultValue();
        Optional<Holder.Reference<T>> value = getConfigEntry().getRegistry().get(Identifier.parse(this.editBoxWidget.getValue()));
        return value.isPresent() && defaultValue == value.get().value();
    }

    @Override
    public void resetValue() {
        Identifier resourceLocation = getConfigEntry().getRegistry().getKey(getConfigEntry().getDefaultValue());
        String stringValue = resourceLocation != null ? resourceLocation.toString() : "unknown";
        this.editBoxWidget.setValue(stringValue);
    }

    @Override
    public void applyValue() {
        if (this.editBoxWidget.hasInputValidationErrors()) return;
        Optional<Holder.Reference<T>> reference = getConfigEntry().getRegistry().get(Identifier.parse(this.editBoxWidget.getValue()));
        reference.ifPresent(tReference -> getConfigEntry().set(tReference.value()));
    }

    @Override
    public boolean hasValidationErrors() {
        return this.editBoxWidget.hasInputValidationErrors();
    }
}