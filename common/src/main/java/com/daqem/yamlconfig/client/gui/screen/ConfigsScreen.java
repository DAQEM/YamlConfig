package com.daqem.yamlconfig.client.gui.screen;

import com.daqem.uilib.gui.AbstractScreen;
import com.daqem.uilib.gui.background.BlurredBackground;
import com.daqem.uilib.gui.component.sprite.SpriteComponent;
import com.daqem.uilib.gui.component.text.TextComponent;
import com.daqem.uilib.gui.widget.ButtonWidget;
import com.daqem.uilib.gui.widget.ScrollContainerWidget;
import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.api.config.IConfig;
import com.daqem.yamlconfig.client.gui.component.ConfigsCategoryComponent;
import com.daqem.yamlconfig.client.gui.component.EmptyComponent;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ConfigsScreen extends AbstractScreen {

    private final Map<String, List<IConfig>> configs;

    public ConfigsScreen(Map<String, List<IConfig>> configs) {
        super(YamlConfig.translatable("screen.configs"));
        this.configs = new TreeMap<>(configs);
        this.setBackground(new BlurredBackground());
    }

    @Override
    public void init() {
        SpriteComponent headerBackground = new SpriteComponent(0, 32, this.width, 2, YamlConfig.getId("header_separator"));
        SpriteComponent contentBackground = new SpriteComponent(0, 34, this.width, this.height - 66, YamlConfig.getId("menu_background"));
        SpriteComponent footerBackground = new SpriteComponent(0, this.height - 32, this.width, 2, YamlConfig.getId("footer_separator"));

        TextComponent title = new TextComponent(0, 10, getTitle());
        title.setDrawShadow(true);
        title.centerHorizontally();

        ButtonWidget doneButton = new ButtonWidget(width / 2 - (150 / 2), height - 27, 150, 20, Component.translatable("gui.done"), button -> this.onClose());

        List<ConfigsCategoryComponent> configCategories = new ArrayList<>();
        this.configs.values().forEach(modConfigs ->
                configCategories.add(new ConfigsCategoryComponent(0, 0, getFont(), modConfigs))
        );

        EmptyComponent scrollContainerComponent = new EmptyComponent(this.width / 2 - (310 / 2), 34, 310, this.height - 34 - 32);

        ScrollContainerWidget scrollContainerWidget = new ScrollContainerWidget(310, this.height - 34 - 32);

        configCategories.forEach(scrollContainerWidget::addComponent);

        scrollContainerComponent.addWidget(scrollContainerWidget);

        this.addComponent(headerBackground);
        this.addComponent(footerBackground);
        this.addComponent(contentBackground);
        this.addWidget(doneButton);
        this.addComponent(title);
        this.addComponent(scrollContainerComponent);

        super.init();
    }
}
