package com.daqem.yamlconfig.client.gui.screen;

import com.daqem.uilib.gui.AbstractScreen;
import com.daqem.uilib.gui.background.BlurredBackground;
import com.daqem.uilib.gui.component.sprite.SpriteComponent;
import com.daqem.uilib.gui.component.text.TextComponent;
import com.daqem.uilib.gui.widget.ButtonWidget;
import com.daqem.uilib.gui.widget.ScrollContainerWidget;
import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.api.config.ConfigType;
import com.daqem.yamlconfig.api.config.IConfig;
import com.daqem.yamlconfig.api.gui.component.IConfigEntryComponent;
import com.daqem.yamlconfig.client.gui.ConfigEntryComponentBuilder;
import com.daqem.yamlconfig.client.gui.component.ConfigCategoryComponent;
import com.daqem.yamlconfig.client.gui.component.EmptyComponent;
import com.daqem.yamlconfig.client.gui.component.MarginComponent;
import com.daqem.yamlconfig.client.gui.component.entry.BaseConfigEntryComponent;
import com.daqem.yamlconfig.networking.c2s.ServerboundSaveConfigPacket;
import dev.architectury.networking.NetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.List;

public class ConfigScreen extends AbstractScreen {

    private final Screen previousScreen;
    private final IConfig config;

    public ConfigScreen(Screen previousScreen, IConfig config) {
        super(Component.empty().append(config.getModName()).append(Component.literal(" - ")).append(config.getDisplayName()));
        this.previousScreen = previousScreen;
        this.config = config;
        setBackground(new BlurredBackground());
    }

    @Override
    public void init() {
        SpriteComponent headerBackground = new SpriteComponent(0, 32, this.width, 2, YamlConfig.getId("header_separator"));
        SpriteComponent contentBackground = new SpriteComponent(0, 34, this.width, this.height - 66, YamlConfig.getId("menu_background"));
        SpriteComponent footerBackground = new SpriteComponent(0, this.height - 32, this.width, 2, YamlConfig.getId("footer_separator"));

        TextComponent title = new TextComponent(0, 10, getTitle());
        title.setDrawShadow(true);
        title.centerHorizontally();

        ButtonWidget cancelButton = new ButtonWidget(width / 2 - 152, height - 27, 150, 20, YamlConfig.translatable("screen.config.cancel"), button -> this.onClose());

        ConfigCategoryComponent configCategoryComponent = new ConfigEntryComponentBuilder(this.config).build();

        ButtonWidget saveChangesButton = new ButtonWidget(width / 2 + 2, height - 27, 150, 20, YamlConfig.translatable("screen.config.save"), button -> {
            List<IConfigEntryComponent<?>> configEntryComponents = configCategoryComponent.getAllConfigEntryComponents();
            configEntryComponents.forEach(IConfigEntryComponent::applyValue);
            if (this.config.getType() == ConfigType.CLIENT) {
                this.config.save();
            } else {
                NetworkManager.sendToServer(new ServerboundSaveConfigPacket(this.config));
            }

            this.onClose();
        });

        EmptyComponent scrollContainerComponent = new EmptyComponent(this.width / 2 - (330 / 2), 34, 330, this.height - 34 - 32);

        ScrollContainerWidget scrollContainerWidget = new ScrollContainerWidget(330, this.height - 34 - 32);

        scrollContainerWidget.addComponent(new MarginComponent(BaseConfigEntryComponent.TOTAL_WIDTH, 10));
        scrollContainerWidget.addComponent(configCategoryComponent);
        scrollContainerWidget.addComponent(new MarginComponent(BaseConfigEntryComponent.TOTAL_WIDTH, 10));

        scrollContainerComponent.addWidget(scrollContainerWidget);

//        ScrollContentComponent content = new ScrollContentComponent(0, 0, BaseConfigEntryComponent.GAP_WIDTH, ScrollOrientation.VERTICAL);
//        ScrollWheelComponent scrollWheel = new ScrollWheelComponent(Textures.SCROLL_WHEEL, 0, 0, 6);
//        ScrollBarComponent scrollBar = new ScrollBarComponent(BaseConfigEntryComponent.TOTAL_WIDTH + BaseConfigEntryComponent.GAP_WIDTH, 0, 6, getHeight() - 34 - 32, ScrollOrientation.VERTICAL, scrollWheel);
//        scrollBar.setBackground(null);
//
//        this.scrollPanel = new ScrollPanelComponent(
//                null, 0, 34, BaseConfigEntryComponent.TOTAL_WIDTH, getHeight() - 34 - 32,
//                ScrollOrientation.VERTICAL, content, scrollBar);
//
//        this.scrollPanel.centerHorizontally();
//
//        content.addChild(new MarginComponent(BaseConfigEntryComponent.TOTAL_WIDTH, 10));
//        content.addChild(configCategoryComponent);
//        content.addChild(new MarginComponent(BaseConfigEntryComponent.TOTAL_WIDTH, 10));

        this.addComponent(headerBackground);
        this.addComponent(contentBackground);
        this.addComponent(footerBackground);
        this.addComponent(title);
        this.addWidget(cancelButton);
        this.addWidget(saveChangesButton);
        this.addComponent(scrollContainerComponent);

        super.init();
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
