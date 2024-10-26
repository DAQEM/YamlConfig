package com.daqem.yamlconfig.client.gui.component.entry.minecraft;

import com.daqem.uilib.client.gui.component.io.TextBoxComponent;
import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.client.gui.component.entry.BaseConfigEntryComponent;
import com.daqem.yamlconfig.impl.config.entry.minecraft.RegistryConfigEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Objects;

public class RegistryConfigEntryComponent<T> extends BaseConfigEntryComponent<RegistryConfigEntryComponent<T>, RegistryConfigEntry<T>> {

    private final TextBoxComponent textBoxComponent;

    public RegistryConfigEntryComponent(String key, RegistryConfigEntry<T> configEntry) {
        super(key, configEntry, 0, 0, DEFAULT_HEIGHT);

        this.textBoxComponent = new TextBoxComponent(KEY_WIDTH + GAP_WIDTH, 0, VALUE_WIDTH, DEFAULT_HEIGHT, Objects.requireNonNull(getConfigEntry().getRegistry().getKey(getConfigEntry().get())).toString()) {
            @Override
            public List<Component> validateInput(String input) {
                List<Component> list = super.validateInput(input);
                ResourceLocation value = ResourceLocation.tryParse(input);
                if (value == null || value.getPath().isEmpty() || value.getNamespace().isEmpty() || value.getPath().contains(" ") || value.getNamespace().contains(" ")){
                    list.add(YamlConfig.translatable("gui.validation_error.invalid_resource_location"));
                } else {
                    if (!getConfigEntry().getRegistry().keySet().contains(value)) {
                        list.add(YamlConfig.translatable("gui.validation_error.invalid_registry_value"));
                    }
                }
                return list;
            }
        };

        textBoxComponent.setMaxLength(Integer.MAX_VALUE);
    }

    @Override
    public void startRenderable() {
        this.addChild(this.textBoxComponent);
        super.startRenderable();
    }

    @Override
    public boolean isOriginalValue() {
        return getConfigEntry().get() == getConfigEntry().getRegistry().get(ResourceLocation.parse(this.textBoxComponent.getValue()));
    }

    @Override
    public void resetValue() {
        this.textBoxComponent.setValue(Objects.requireNonNull(getConfigEntry().getRegistry().getKey(getConfigEntry().get())).toString());
    }

    @Override
    public void applyValue() {
        if (this.textBoxComponent.hasInputValidationErrors()) return;
        getConfigEntry().setValue(getConfigEntry().getRegistry().get(ResourceLocation.parse(this.textBoxComponent.getValue())));
    }
}
