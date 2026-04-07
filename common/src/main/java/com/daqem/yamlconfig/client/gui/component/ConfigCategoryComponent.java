package com.daqem.yamlconfig.client.gui.component;

import com.daqem.uilib.gui.component.AbstractComponent;
import com.daqem.yamlconfig.api.config.entry.IStackConfigEntry;
import com.daqem.yamlconfig.api.gui.component.IConfigEntryComponent;
import com.daqem.yamlconfig.client.gui.component.entry.BaseConfigEntryComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ConfigCategoryComponent extends AbstractComponent {

    private final @Nullable String key;
    private final List<AbstractComponent> children;

    public ConfigCategoryComponent(IStackConfigEntry stackConfigEntry, @Nullable String key, List<AbstractComponent> children) {
        super(0, 0, BaseConfigEntryComponent.TOTAL_WIDTH, 0);
        this.key = key;
        this.children = children;

        if (key != null) {
            this.addComponent(new TruncatedKeyTextComponent(key, getWidth(), stackConfigEntry, true));
        }
        this.addComponents(children);
    }

    @Override
    public int getWidth() {
        return BaseConfigEntryComponent.TOTAL_WIDTH;
    }

    @Override
    public int getHeight() {
        int height = 0;

        // FIX: Matched the height calculation to the render offset (lineHeight + 12)
        if (this.key != null) {
            height += Minecraft.getInstance().font.lineHeight + 12;
        }

        for (int i = 0; i < this.children.size(); i++) {
            AbstractComponent child = this.children.get(i);
            height += child.getHeight();

            // Add gap after every element except the last one
            if (i < this.children.size() - 1) {
                height += 10;
            }
        }
        return height;
    }

    @Override
    public void render(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick, int parentWidth, int parentHeight) {
        renderHorizontalLines(guiGraphics);
        int currentY = 0;

        // Offset for the Category Title
        if (this.key != null) {
            currentY += Minecraft.getInstance().font.lineHeight + 12;
        }

        for (AbstractComponent child : this.children) {
            child.setY(currentY);
            child.render(guiGraphics, mouseX, mouseY, partialTick, parentWidth, parentHeight);

            // Add height + gap for the next element's position
            currentY += child.getHeight() + 10;
        }
    }

    private void renderHorizontalLines(GuiGraphicsExtractor graphics) {
        if (this.key == null) return;

        int lineYStart = Minecraft.getInstance().font.lineHeight + 6;
        graphics.fill(getTotalX(), getTotalY() + lineYStart, getTotalX() + getWidth(), getTotalY() + lineYStart + 1, 0xFFFFFFFF);
    }

    public List<IConfigEntryComponent<?>> getAllConfigEntryComponents() {
        List<IConfigEntryComponent<?>> allComponents = new ArrayList<>();
        for (AbstractComponent child : children) {
            if (child instanceof IConfigEntryComponent<?> entryComponent) {
                allComponents.add(entryComponent);
            } else if (child instanceof ConfigCategoryComponent categoryComponent) {
                allComponents.addAll(categoryComponent.getAllConfigEntryComponents());
            }
        }
        return allComponents;
    }
}