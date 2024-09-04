package com.daqem.yamlconfig.client.gui.component;

import com.daqem.uilib.api.client.gui.component.scroll.ScrollOrientation;
import com.daqem.uilib.client.gui.component.scroll.ScrollContentComponent;
import com.daqem.yamlconfig.api.gui.component.IConfigEntryComponent;
import com.daqem.yamlconfig.client.gui.component.entry.BaseConfigEntryComponent;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ConfigCategoryComponent extends ScrollContentComponent {

    private final List<IConfigEntryComponent<?, ?>> configEntryComponents;
    private final List<ConfigCategoryComponent> subCategories;

    private final @Nullable TruncatedKeyTextComponent keyText;

    public ConfigCategoryComponent(@Nullable String key, List<IConfigEntryComponent<?, ?>> configEntryComponents) {
        this(key, configEntryComponents, new ArrayList<>());
    }

    public ConfigCategoryComponent(@Nullable String key, List<IConfigEntryComponent<?, ?>> configEntryComponents, List<ConfigCategoryComponent> subCategories) {
        super(0, 0, BaseConfigEntryComponent.GAP_WIDTH, ScrollOrientation.VERTICAL);
        this.configEntryComponents = configEntryComponents;
        this.subCategories = subCategories;
        if (key == null) {
            this.keyText = null;
        } else {
            this.keyText = new TruncatedKeyTextComponent(key, getWidth(), BaseConfigEntryComponent.DEFAULT_HEIGHT);
            if (this.keyText.getText() != null) {
            this.keyText.getText().setBold(true);
            }
        }
    }

    @Override
    public int getWidth() {
        return BaseConfigEntryComponent.TOTAL_WIDTH;
    }

    @Override
    public void startRenderable() {
        if (this.keyText != null) {
            this.addChildren(this.keyText);
        }
        this.configEntryComponents.forEach(this::addChild);
        this.subCategories.forEach(this::addChild);
        super.startRenderable();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);
        renderHorizontalLines(graphics);
    }

    public void addSubCategory(ConfigCategoryComponent subCategory) {
        if (subCategory == this) {
            throw new IllegalArgumentException("Cannot add a category to itself");
        }
        if (!this.subCategories.contains(subCategory)) {
            this.subCategories.add(subCategory);
        }
    }

    private void renderHorizontalLines(GuiGraphics graphics) {
        if (this.keyText == null) {
            return;
        }
        int lineYStart = Objects.requireNonNull(this.keyText.getText()).getFont().lineHeight + 6;
        graphics.fill(0, lineYStart, getWidth(), lineYStart + 1, 0xFFFFFFFF);
    }
}
