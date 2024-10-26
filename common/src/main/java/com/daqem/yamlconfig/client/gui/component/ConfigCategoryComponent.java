package com.daqem.yamlconfig.client.gui.component;

import com.daqem.uilib.api.client.gui.component.scroll.ScrollOrientation;
import com.daqem.uilib.client.gui.component.scroll.ScrollContentComponent;
import com.daqem.yamlconfig.api.gui.component.IConfigEntryComponent;
import com.daqem.yamlconfig.client.gui.component.entry.BaseConfigEntryComponent;
import net.minecraft.client.Minecraft;
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
            this.addChild(this.keyText);
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

    @Override
    public void preformOnHoverEvent(double mouseX, double mouseY, float delta) {
        if (this.keyText != null && this.keyText.getText() != null) {
            if (isTotalHovered(mouseX, mouseY)) {
                this.keyText.getText().setTextColor(0xFFE0E0E0);
            } else {
                this.keyText.getText().setTextColor(0xFFFFFFFF);
            }
        }
        if (getOnHoverEvent() != null) {
            if (this.isTotalHovered(mouseX, mouseY)) {
                getOnHoverEvent().onHover(getHoverState(), Minecraft.getInstance().screen, mouseX, mouseY, delta);
            }
        }
    }

    @Override
    public boolean preformOnClickEvent(double mouseX, double mouseY, int button) {
        if (getOnClickEvent() != null) {
            if (this.isTotalHovered(mouseX, mouseY)) {
                return getOnClickEvent().onClick(this, Minecraft.getInstance().screen, mouseX, mouseY, button);
            }
        }
        return false;
    }

    @Override
    public boolean preformOnMouseReleaseEvent(double mouseX, double mouseY, int button) {
        if (getOnMouseReleaseEvent() != null) {
            if (this.isTotalHovered(mouseX, mouseY)) {
                return getOnMouseReleaseEvent().onMouseRelease(this, Minecraft.getInstance().screen, mouseX, mouseY, button);
            }
        }
        return false;
    }

    @Override
    public boolean preformOnCharTypedEvent(char typedChar, int modifiers) {
        if (getOnCharTypedEvent() != null) {
            return getOnCharTypedEvent().onCharTyped(this, Minecraft.getInstance().screen, typedChar, modifiers);
        }
        return false;
    }

    @Override
    public boolean preformOnKeyPressedEvent(int keyCode, int scanCode, int modifiers) {
        if (getOnKeyPressedEvent() != null) {
            return getOnKeyPressedEvent().onKeyPressed(this, Minecraft.getInstance().screen, keyCode, scanCode, modifiers);
        }
        return false;
    }

    @Override
    public boolean preformOnDragEvent(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (getOnDragEvent() != null) {
            if (this.isTotalHovered(mouseX, mouseY)) {
                return getOnDragEvent().onDrag(this, Minecraft.getInstance().screen, mouseX, mouseY, button, dragX, dragY);
            }
        }
        return false;
    }

    @Override
    public boolean preformOnScrollEvent(double mouseX, double mouseY, double amountX, double amountY) {
        if (getOnScrollEvent() != null) {
            if (this.isTotalHovered(mouseX, mouseY)) {
                return getOnScrollEvent().onScroll(this, Minecraft.getInstance().screen, mouseX, mouseY, amountX, amountY);
            }
        }
        return false;
    }

//    public StackConfigEntry createConfigEntryCopy() {
//        return new StackConfigEntry(
//
//        );
//    }
}
