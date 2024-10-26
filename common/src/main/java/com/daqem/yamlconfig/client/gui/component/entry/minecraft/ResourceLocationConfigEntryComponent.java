package com.daqem.yamlconfig.client.gui.component.entry.minecraft;

import com.daqem.uilib.client.gui.component.io.TextBoxComponent;
import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.client.gui.component.entry.BaseConfigEntryComponent;
import com.daqem.yamlconfig.impl.config.entry.minecraft.ResourceLocationConfigEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class ResourceLocationConfigEntryComponent extends BaseConfigEntryComponent<ResourceLocationConfigEntryComponent, ResourceLocationConfigEntry> {

    private final TextBoxComponent textBoxComponent;

    public ResourceLocationConfigEntryComponent(String key, ResourceLocationConfigEntry configEntry) {
        super(key, configEntry, 0, 0, DEFAULT_HEIGHT);

        this.textBoxComponent = new TextBoxComponent(KEY_WIDTH + GAP_WIDTH, 0, VALUE_WIDTH, DEFAULT_HEIGHT, this.getConfigEntry().get().toString()) {
            @Override
            public List<Component> validateInput(String input) {
                List<Component> list = super.validateInput(input);
                ResourceLocation value = ResourceLocation.tryParse(input);
                if (value == null || value.getPath().isEmpty() || value.getNamespace().isEmpty() || value.getPath().contains(" ") || value.getNamespace().contains(" ")){
                    list.add(YamlConfig.translatable("gui.validation_error.invalid_resource_location"));
                } else {
                    if (configEntry.getPattern() != null && !input.matches(configEntry.getPattern())) {
                        list.add(YamlConfig.translatable("gui.validation_error.pattern", configEntry.getPattern()));
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
    public void renderTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        if (isTotalHovered(mouseX, mouseY)) {
            if (!this.textBoxComponent.hasInputValidationErrors() && !this.textBoxComponent.getValue().contains(":")) {
                ResourceLocation resourceLocation = ResourceLocation.tryParse(this.textBoxComponent.getValue());
                guiGraphics.renderTooltip(Minecraft.getInstance().font,
                        Component.literal(resourceLocation == null
                                ? "Invalid Resource Location"
                                : resourceLocation.toString()),
                        mouseX, mouseY);
            }
        }
    }

    @Override
    public boolean isOriginalValue() {
        return this.getConfigEntry().get().equals(ResourceLocation.tryParse(this.textBoxComponent.getValue()));
    }

    @Override
    public void resetValue() {
        this.textBoxComponent.setValue(this.getConfigEntry().get().toString());
    }

    @Override
    public void applyValue() {
        if (this.textBoxComponent.hasInputValidationErrors()) return;
        this.getConfigEntry().set(ResourceLocation.tryParse(this.textBoxComponent.getValue()));
    }
}
