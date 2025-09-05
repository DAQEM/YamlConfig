package com.daqem.yamlconfig.client.gui.component.entry.minecraft;

import com.daqem.uilib.gui.widget.EditBoxWidget;
import com.daqem.yamlconfig.client.gui.component.entry.BaseConfigEntryComponent;
import com.daqem.yamlconfig.impl.config.entry.minecraft.RegistryConfigEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;
import java.util.Optional;

public class RegistryConfigEntryComponent<T> extends BaseConfigEntryComponent<RegistryConfigEntry<T>> {

    private final EditBoxWidget editBoxWidget;

    public RegistryConfigEntryComponent(String key, RegistryConfigEntry<T> configEntry) {
        super(key, configEntry, 0, 0, DEFAULT_HEIGHT);

        this.editBoxWidget = new EditBoxWidget(
                Minecraft.getInstance().font,
                KEY_WIDTH + GAP_WIDTH,
                0,
                VALUE_WIDTH,
                DEFAULT_HEIGHT,
                Component.empty()
        ) {
//            @Override
//            public List<Component> validateInput(String input) {
//                List<Component> list = super.validateInput(input);
//                ResourceLocation value = ResourceLocation.tryParse(input);
//                if (value == null || value.getPath().isEmpty() || value.getNamespace().isEmpty() || value.getPath().contains(" ") || value.getNamespace().contains(" ")){
//                    list.add(YamlConfig.translatable("gui.validation_error.invalid_resource_location"));
//                } else {
//                    if (!getConfigEntry().getRegistry().keySet().contains(value)) {
//                        list.add(YamlConfig.translatable("gui.validation_error.invalid_registry_value"));
//                    }
//                }
//                return list;
//            } //TODO
        };

        editBoxWidget.setMaxLength(Integer.MAX_VALUE);

        this.addWidget(editBoxWidget);
    }

    @Override
    public boolean isOriginalValue() {
        return getConfigEntry().get() == getConfigEntry().getRegistry().get(ResourceLocation.parse(this.editBoxWidget.getValue()));
    }

    @Override
    public void resetValue() {
        this.editBoxWidget.setValue(Objects.requireNonNull(getConfigEntry().getRegistry().getKey(getConfigEntry().get())).toString());
    }

    @Override
    public void applyValue() {
//        if (this.editBoxWidget.hasInputValidationErrors()) return; //TODO
        Optional<Holder.Reference<T>> reference = getConfigEntry().getRegistry().get(ResourceLocation.parse(this.editBoxWidget.getValue()));
        reference.ifPresent(tReference -> getConfigEntry().set(tReference.value()));
    }
}
