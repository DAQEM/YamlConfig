package com.daqem.yamlconfig.client.gui.screen;

import com.daqem.uilib.api.client.gui.component.IComponent;
import com.daqem.uilib.api.client.gui.component.scroll.ScrollOrientation;
import com.daqem.uilib.client.gui.AbstractScreen;
import com.daqem.uilib.client.gui.background.GradientBackground;
import com.daqem.uilib.client.gui.component.SolidColorComponent;
import com.daqem.uilib.client.gui.component.TextComponent;
import com.daqem.uilib.client.gui.component.io.ButtonComponent;
import com.daqem.uilib.client.gui.component.scroll.ScrollBarComponent;
import com.daqem.uilib.client.gui.component.scroll.ScrollContentComponent;
import com.daqem.uilib.client.gui.component.scroll.ScrollPanelComponent;
import com.daqem.uilib.client.gui.component.scroll.ScrollWheelComponent;
import com.daqem.uilib.client.gui.component.texture.TextureComponent;
import com.daqem.uilib.client.gui.text.Text;
import com.daqem.uilib.client.gui.texture.Texture;
import com.daqem.uilib.client.gui.texture.Textures;
import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.api.config.ConfigType;
import com.daqem.yamlconfig.api.config.IConfig;
import com.daqem.yamlconfig.api.gui.component.IConfigEntryComponent;
import com.daqem.yamlconfig.client.gui.ConfigEntryComponentBuilder;
import com.daqem.yamlconfig.client.gui.component.ConfigCategoryComponent;
import com.daqem.yamlconfig.client.gui.component.MarginComponent;
import com.daqem.yamlconfig.client.gui.component.entry.BaseConfigEntryComponent;
import com.daqem.yamlconfig.networking.c2s.ServerboundSaveConfigPacket;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.architectury.networking.NetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigScreen extends AbstractScreen {

    private final Screen previousScreen;
    private final IConfig config;

    private TextureComponent headerBackground;
    private SolidColorComponent contentBackground;
    private TextureComponent footerBackground;

    private TextComponent title;
    private ButtonComponent saveChangesButton;
    private ButtonComponent cancelButton;

    private ScrollPanelComponent scrollPanel;

    public ConfigScreen(Screen previousScreen, IConfig config) {
        super(Component.empty().append(config.getModName()).append(Component.literal(" - ")).append(config.getDisplayName()));
        this.previousScreen = previousScreen;
        this.config = config;
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


        this.cancelButton = new ButtonComponent(width / 2 - 152, height - 27, 150, 20, YamlConfig.translatable("screen.config.cancel"), (clickedObject, screen, mouseX, mouseY, button) -> {
            this.onClose();
            return true;
        });

        ConfigCategoryComponent configCategoryComponent = new ConfigEntryComponentBuilder(this.config).build();

        this.saveChangesButton = new ButtonComponent(width / 2 + 2, height - 27, 150, 20, YamlConfig.translatable("screen.config.save"), (clickedObject, screen, mouseX, mouseY, button) -> {
            List<IConfigEntryComponent<?, ?>> configEntryComponents = configCategoryComponent.getAllConfigEntryComponents();
            configEntryComponents.forEach(IConfigEntryComponent::applyValue);
            if (this.config.getType() == ConfigType.CLIENT) {
                this.config.save();
            } else {
                NetworkManager.sendToServer(new ServerboundSaveConfigPacket(this.config));
            }

            this.onClose();
            return true;
        });



        ScrollContentComponent content = new ScrollContentComponent(0, 0, BaseConfigEntryComponent.GAP_WIDTH, ScrollOrientation.VERTICAL);
        ScrollWheelComponent scrollWheel = new ScrollWheelComponent(Textures.SCROLL_WHEEL, 0, 0, 6);
        ScrollBarComponent scrollBar = new ScrollBarComponent(BaseConfigEntryComponent.TOTAL_WIDTH + BaseConfigEntryComponent.GAP_WIDTH, 0, 6, getHeight() - 34 - 32, ScrollOrientation.VERTICAL, scrollWheel);
        scrollBar.setBackground(null);

        this.scrollPanel = new ScrollPanelComponent(
                null, 0, 34, BaseConfigEntryComponent.TOTAL_WIDTH, getHeight() - 34 - 32,
                ScrollOrientation.VERTICAL, content, scrollBar);

        this.scrollPanel.centerHorizontally();

        content.addChild(new MarginComponent(BaseConfigEntryComponent.TOTAL_WIDTH, 10));
        content.addChild(configCategoryComponent);
        content.addChild(new MarginComponent(BaseConfigEntryComponent.TOTAL_WIDTH, 10));

        this.addComponents(this.title, this.saveChangesButton, this.cancelButton, this.scrollPanel);
    }

    @Override
    public void onResizeScreenRepositionComponents(int width, int height) {
        super.onResizeScreenRepositionComponents(width, height);
        this.headerBackground.setWidth(width);
        this.contentBackground.setWidth(width);
        this.contentBackground.setHeight(height - 66);
        this.contentBackground.setY(34);
        this.footerBackground.setWidth(width);
        this.footerBackground.setY(height - 32);
        this.cancelButton.setX(width / 2 - (this.cancelButton.getWidth() + 2));
        this.cancelButton.setY(height - 27);
        this.saveChangesButton.setX(width / 2 + (2));
        this.saveChangesButton.setY(height - 27);
        this.scrollPanel.setHeight(height - 34 - 32);
        this.scrollPanel.getScrollBar().ifPresent(scrollBar -> scrollBar.setHeight(height - 34 - 32));
    }

    @Override
    public void onTickScreen(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {

    }

    @Override
    public void renderComponents(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        this.renderBlurredBackground(delta);
        RenderSystem.enableBlend();
        this.headerBackground.renderBase(guiGraphics, mouseX, mouseY, delta);
        this.footerBackground.renderBase(guiGraphics, mouseX, mouseY, delta);
        RenderSystem.disableBlend();
        this.contentBackground.renderBase(guiGraphics, mouseX, mouseY, delta);
        super.renderComponents(guiGraphics, mouseX, mouseY, delta);
    }

    @Override
    public void onClose() {
        if (this.previousScreen != null) {
            Minecraft.getInstance().setScreen(this.previousScreen);
        } else {
            super.onClose();
        }
    }
}
