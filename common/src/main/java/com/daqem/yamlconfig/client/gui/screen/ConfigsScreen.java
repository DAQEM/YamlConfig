package com.daqem.yamlconfig.client.gui.screen;

import com.daqem.uilib.gui.AbstractScreen;
import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.api.config.IConfig;
import com.daqem.yamlconfig.client.gui.component.ConfigsCategoryComponent;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ConfigsScreen extends AbstractScreen {

    private final Map<String, List<IConfig>> configs;

    private TextureComponent headerBackground;
    private SolidColorComponent contentBackground;
    private TextureComponent footerBackground;

    private TextComponent title;
    private ButtonComponent doneButton;

    private final List<ConfigsCategoryComponent> configCategories = new ArrayList<>();
    private ScrollPanelComponent categoriesScrollPanel;

    public ConfigsScreen(Map<String, List<IConfig>> configs) {
        super(YamlConfig.translatable("screen.configs"));
        this.configs = new TreeMap<>(configs);
    }

    @Override
    public void startScreen() {
        this.setBackground(new GradientBackground(getWidth(), getHeight(), 0, 0));

        this.headerBackground = new TextureComponent(new Texture(HEADER_SEPARATOR, 0, 0, 32, 2, 32, 2), 0, 32, getWidth(), 2);
        this.contentBackground = new SolidColorComponent(0, 34, getWidth(), getHeight() - 66, 0x70000000);
        this.footerBackground = new TextureComponent(new Texture(FOOTER_SEPARATOR, 0, 0, 32, 2, 32, 2), 0, getHeight() - 32, getWidth(), 2);

        Text titleText = new Text(getFont(), getTitle());
        titleText.setShadow(true);
        this.title = new TextComponent(0, 10, titleText);
        this.title.centerHorizontally();

        this.doneButton = new ButtonComponent(width / 2 - (150 / 2), height - 27, 150, 20, Component.translatable("gui.done"), (clickedObject, screen, mouseX, mouseY, button) -> {
            this.onClose();
            return true;
        });

        this.configs.values().forEach(modConfigs ->
                this.configCategories.add(new ConfigsCategoryComponent(0, 0, getFont(), modConfigs))
        );


        ScrollContentComponent content = new ScrollContentComponent(0, 0, 0, ScrollOrientation.VERTICAL);
        ScrollWheelComponent scrollWheel = new ScrollWheelComponent(Textures.SCROLL_WHEEL, 0, 0, 6);
        ScrollBarComponent scrollBar = new ScrollBarComponent(305, 0, 6, getHeight() - 34 - 32, ScrollOrientation.VERTICAL, scrollWheel);
        scrollBar.setBackground(null);

        this.categoriesScrollPanel = new ScrollPanelComponent(
                null, 0, 34, 300, getHeight() - 34 - 32,
                ScrollOrientation.VERTICAL, content, scrollBar);

        this.categoriesScrollPanel.centerHorizontally();

        this.configCategories.forEach(content::addChild);

        this.addComponents(doneButton);

        this.addComponents(this.title, this.categoriesScrollPanel);
    }

    @Override
    public void onResizeScreenRepositionComponents(int width, int height) {
        this.headerBackground.setWidth(width);
        this.contentBackground.setWidth(width);
        this.contentBackground.setHeight(height - 66);
        this.contentBackground.setY(34);
        this.footerBackground.setWidth(width);
        this.footerBackground.setY(height - 32);
        this.doneButton.setX(width / 2 - (this.doneButton.getWidth() / 2));
        this.doneButton.setY(height - 27);
        this.categoriesScrollPanel.setHeight(height - 34 - 32);
        this.categoriesScrollPanel.getScrollBar().ifPresent(scrollBar -> scrollBar.setHeight(height - 34 - 32));
    }

    @Override
    public void onTickScreen(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
    }

    @Override
    public void renderComponents(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        this.renderBlurredBackground();
        this.headerBackground.renderBase(guiGraphics, mouseX, mouseY, delta);
        this.footerBackground.renderBase(guiGraphics, mouseX, mouseY, delta);
        this.contentBackground.renderBase(guiGraphics, mouseX, mouseY, delta);
        super.renderComponents(guiGraphics, mouseX, mouseY, delta);
    }
}
