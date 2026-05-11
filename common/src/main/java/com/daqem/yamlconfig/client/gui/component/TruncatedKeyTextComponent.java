package com.daqem.yamlconfig.client.gui.component;

import com.daqem.uilib.gui.component.text.TruncatedTextComponent;
import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public class TruncatedKeyTextComponent extends TruncatedTextComponent {

    private final @Nullable IConfigEntry<?> configEntry;

    public TruncatedKeyTextComponent(String key, int maxWidth, @Nullable IConfigEntry<?> configEntry, boolean bold) {
        super(0, 5, maxWidth, YamlConfig.translatable(key).withStyle(Style.EMPTY.withBold(bold)));
        this.configEntry = configEntry;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, int parentWidth, int parentHeight) {
        super.render(guiGraphics, mouseX, mouseY, partialTick, parentWidth, parentHeight);
        if (configEntry != null && this.getRectangle().containsPoint(mouseX, mouseY)) {
            List<String> comments = configEntry
                    .getComments()
                    .getComments(false);

            if (!comments.isEmpty()) {
                guiGraphics.renderComponentTooltip(
                        getFont(),
                        comments.stream().map(Component::literal).collect(Collectors.toList()),
                        mouseX,
                        mouseY);
            }
        }
    }

    @Override
    public @NotNull ScreenRectangle getRectangle() {
        return new ScreenRectangle(this.getTotalX(), this.getTotalY(), this.getWidth(), this.getHeight());
    }
}
