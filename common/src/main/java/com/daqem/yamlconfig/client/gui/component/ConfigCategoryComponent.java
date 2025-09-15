package com.daqem.yamlconfig.client.gui.component;

import com.daqem.uilib.gui.component.AbstractComponent;
import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.config.entry.IStackConfigEntry;
import com.daqem.yamlconfig.api.gui.component.IConfigEntryComponent;
import com.daqem.yamlconfig.client.gui.component.entry.BaseConfigEntryComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ConfigCategoryComponent extends AbstractComponent {

    private final IStackConfigEntry stackConfigEntry;
    private final @Nullable String key;
    private final List<IConfigEntryComponent<?>> configEntryComponents;
    private final List<ConfigCategoryComponent> subCategories;


    public ConfigCategoryComponent(IStackConfigEntry stackConfigEntry, @Nullable String key, List<IConfigEntryComponent<?>> configEntryComponents) {
        this(stackConfigEntry, key, configEntryComponents, new ArrayList<>());
    }

    public ConfigCategoryComponent(IStackConfigEntry stackConfigEntry, @Nullable String key, List<IConfigEntryComponent<?>> configEntryComponents, List<ConfigCategoryComponent> subCategories) {
        super(0, 0, BaseConfigEntryComponent.TOTAL_WIDTH, 0);
        this.stackConfigEntry = stackConfigEntry;
        this.key = key;
        this.configEntryComponents = configEntryComponents;
        this.subCategories = subCategories;

        if (key != null) {
            this.addComponent(new TruncatedKeyTextComponent(key, getWidth(), stackConfigEntry, true));
        }

        this.addComponents(configEntryComponents);
        this.addComponents(subCategories);
    }

    @Override
    public int getWidth() {
        return BaseConfigEntryComponent.TOTAL_WIDTH;
    }

    @Override
    public int getHeight() {
        int height = 0;
        if (this.key != null) {
            height += Minecraft.getInstance().font.lineHeight + 6;
        }
        for (IConfigEntryComponent<?> entry : this.configEntryComponents) {
            height += entry.getHeight() + 10;
        }
        for (int i = 0; i < this.subCategories.size(); i++) {
            if (i < this.subCategories.size() - 1) {
                height += 10;
            }
            height += this.subCategories.get(i).getHeight();
        }
        return height;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, int parentWidth, int parentHeight) {
        renderHorizontalLines(guiGraphics);
        int currentY = 0;
        if (this.key != null) {
            currentY += Minecraft.getInstance().font.lineHeight + 12;
        }
        for (IConfigEntryComponent<?> entry : this.configEntryComponents) {
            entry.setY(currentY);
            currentY += entry.getHeight() + 10;
        }
        for (int i = 0; i < this.subCategories.size(); i++) {
            ConfigCategoryComponent subCategory = this.subCategories.get(i);
            if (i < this.subCategories.size() - 1) {
                currentY += 10;
            }
            subCategory.setY(currentY);
            currentY += subCategory.getHeight();
        }
    }

    public void addSubCategory(ConfigCategoryComponent subCategory) {
        if (subCategory == this) {
            throw new IllegalArgumentException("Cannot add a category to itself");
        }
        if (!this.subCategories.contains(subCategory)) {
            this.subCategories.add(subCategory);
            this.addComponent(subCategory);
        }
    }

    private void renderHorizontalLines(GuiGraphics graphics) {
        if (this.key == null) return;

        int lineYStart = Minecraft.getInstance().font.lineHeight + 6;
        graphics.fill(getTotalX(), getTotalY() + lineYStart, getTotalX() + getWidth(), getTotalY() + lineYStart + 1, 0xFFFFFFFF);
    }

    public List<IConfigEntryComponent<?>> getAllConfigEntryComponents() {
        List<IConfigEntryComponent<?>> allConfigEntryComponents = new ArrayList<>(this.configEntryComponents);
        this.subCategories.forEach(subCategory -> allConfigEntryComponents.addAll(subCategory.getAllConfigEntryComponents()));
        return allConfigEntryComponents;
    }
}
