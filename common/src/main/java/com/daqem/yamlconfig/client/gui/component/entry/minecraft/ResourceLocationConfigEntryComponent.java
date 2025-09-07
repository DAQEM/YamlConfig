package com.daqem.yamlconfig.client.gui.component.entry.minecraft;

import com.daqem.uilib.gui.widget.EditBoxWidget;
import com.daqem.uilib.util.ValidationErrors;
import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.client.gui.component.entry.BaseConfigEntryComponent;
import com.daqem.yamlconfig.impl.config.entry.minecraft.ResourceLocationConfigEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class ResourceLocationConfigEntryComponent extends BaseConfigEntryComponent<ResourceLocationConfigEntry> {

    private final EditBoxWidget editBoxWidget;

    public ResourceLocationConfigEntryComponent(String key, ResourceLocationConfigEntry configEntry) {
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
                ResourceLocation value = ResourceLocation.tryParse(input);
                if (value == null || value.getPath().isEmpty() || value.getNamespace().isEmpty() || value.getPath().contains(" ") || value.getNamespace().contains(" ")){
                    list.add(ValidationErrors.invalidResourceLocation());
                } else {
                    if (configEntry.getPattern() != null && !input.matches(configEntry.getPattern())) {
                        list.add(ValidationErrors.pattern(configEntry.getPattern()));
                    }
                }
                return list;
            }
        };

        editBoxWidget.setValue(configEntry.get().toString());
        editBoxWidget.setMaxLength(Integer.MAX_VALUE);

        this.addWidget(editBoxWidget);
    }

    @Override
    public boolean isOriginalValue() {
        return this.getConfigEntry().getDefaultValue().equals(ResourceLocation.tryParse(this.editBoxWidget.getValue()));
    }

    @Override
    public void resetValue() {
        this.editBoxWidget.setValue(this.getConfigEntry().getDefaultValue().toString());
    }

    @Override
    public void applyValue() {
        if (this.editBoxWidget.hasInputValidationErrors()) return;
        this.getConfigEntry().set(ResourceLocation.tryParse(this.editBoxWidget.getValue()));
    }

    @Override
    public boolean hasValidationErrors() {
        return this.editBoxWidget.hasInputValidationErrors();
    }
}
