package com.daqem.yamlconfig.client.gui.component;

import com.daqem.uilib.client.gui.component.AbstractComponent;
import com.daqem.uilib.client.gui.component.TextComponent;
import com.daqem.uilib.client.gui.component.io.ButtonComponent;
import com.daqem.uilib.client.gui.text.TruncatedText;
import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.api.config.ConfigType;
import com.daqem.yamlconfig.api.config.IConfig;
import com.daqem.yamlconfig.client.gui.screen.ConfigScreen;
import com.daqem.yamlconfig.networking.c2s.ServerboundOpenConfigScreenPacket;
import dev.architectury.networking.NetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

import java.util.List;

public class ConfigsCategoryComponent extends AbstractComponent<ConfigsCategoryComponent> {

    private static final int WIDTH = 300;
    private static final int TOP_MARGIN = 12;
    private static final int TITLE_HEIGHT = 10;

    private final List<IConfig> configs;
    private final Font font;

    private final TextComponent title;
    private final List<ButtonComponent> configButtons;

    public ConfigsCategoryComponent(int x, int y, Font font, List<IConfig> configs) {
        super(null, x, y, WIDTH, calculateHeight(configs));
        this.configs = configs;
        this.font = font;

        if (configs.isEmpty()) {
            throw new IllegalArgumentException("Configs list cannot be empty");
        }

        IConfig firstConfig = configs.getFirst();
        this.title = new TextComponent(new TruncatedText(this.font, firstConfig.getModName(), 4, TOP_MARGIN, WIDTH, TITLE_HEIGHT));

        this.configButtons = configs.stream()
                .map(config -> new ButtonComponent(0, 0, 144, 20, config.getDisplayName(),
                        (clickedObject, screen, mouseX, mouseY, button) -> {
                            ConfigType type = config.getType();
                            switch (type) {
                                case CLIENT -> Minecraft.getInstance().setScreen(new ConfigScreen(screen, YamlConfig.CONFIG_MANAGER.getConfig(config.getModId(), config.getName())));
                                case COMMON -> Minecraft.getInstance().setScreen(new ConfigScreen(screen, YamlConfig.CONFIG_MANAGER.getConfig(config.getModId(), config.getName())));
                                case SERVER -> NetworkManager.sendToServer(new ServerboundOpenConfigScreenPacket(config.getModId(), config.getName()));
                            }
                            return true;
                        }))
                .toList();

        this.addChild(this.title);
    }

    @Override
    public void startRenderable() {
        this.configButtons.forEach(this::addChild);
        super.startRenderable();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        graphics.fill(0, TOP_MARGIN + TITLE_HEIGHT, getWidth(), TOP_MARGIN + TITLE_HEIGHT + 1, 0xFFFFFFFF);
        this.configButtons.forEach(button -> {
            button.setX(3 + (this.configButtons.indexOf(button) % 2) * 150);
            button.setY((TOP_MARGIN + TITLE_HEIGHT + 3 + (this.configButtons.indexOf(button) / 2) * 24));
        });
    }

    private static int calculateHeight(List<IConfig> configs) {
        float configsPerRow = 2.0F;
        int rowHeight = 24;

        int rows = (int) Math.ceil(configs.size() / configsPerRow);
        return TITLE_HEIGHT + TOP_MARGIN + (rows * rowHeight);
    }
}
