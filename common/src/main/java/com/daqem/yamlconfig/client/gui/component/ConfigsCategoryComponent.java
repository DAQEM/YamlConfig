package com.daqem.yamlconfig.client.gui.component;

import com.daqem.uilib.gui.component.AbstractComponent;
import com.daqem.uilib.gui.component.text.TruncatedTextComponent;
import com.daqem.uilib.gui.widget.ButtonWidget;
import com.daqem.yamlconfig.YamlConfig;
import com.daqem.yamlconfig.api.config.ConfigType;
import com.daqem.yamlconfig.api.config.IConfig;
import com.daqem.yamlconfig.client.gui.screen.ConfigScreen;
import com.daqem.yamlconfig.networking.c2s.ServerboundOpenConfigScreenPacket;
import dev.architectury.networking.NetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;

import java.util.List;

public class ConfigsCategoryComponent extends AbstractComponent {

    private static final int WIDTH = 300;
    private static final int TOP_MARGIN = 12;
    private static final int TITLE_HEIGHT = 10;

    private final List<IConfig> configs;
    private final Font font;

    private final TruncatedTextComponent title;
    private final List<ButtonWidget> configButtons;

    public ConfigsCategoryComponent(int x, int y, Font font, List<IConfig> configs) {
        super(x, y, WIDTH, calculateHeight(configs));
        this.configs = configs;
        this.font = font;

        if (configs.isEmpty()) {
            throw new IllegalArgumentException("Configs list cannot be empty");
        }

        IConfig firstConfig = configs.getFirst();
        this.title = new TruncatedTextComponent(4, TOP_MARGIN, WIDTH, firstConfig.getModName());

        this.configButtons = configs.stream()
                .map(config -> new ButtonWidget(0, 0, 144, 20, config.getDisplayName(),
                        button -> {
                            ConfigType type = config.getType();
                            switch (type) {
                                case CLIENT -> Minecraft.getInstance().setScreen(new ConfigScreen(Minecraft.getInstance().screen, YamlConfig.CONFIG_MANAGER.getConfig(config.getModId(), config.getName())));
                                case COMMON, SERVER -> NetworkManager.sendToServer(new ServerboundOpenConfigScreenPacket(config.getModId(), config.getName()));
                            }
                        }))
                .toList();

        this.addComponent(this.title);
        this.configButtons.forEach(this::addWidget);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, int parentWidth, int parentHeight) {
        guiGraphics.fill(getTotalX(), getTotalY() + TOP_MARGIN + TITLE_HEIGHT, getTotalX() + getWidth(), getTotalY() + TOP_MARGIN + TITLE_HEIGHT + 1, 0xFFFFFFFF);
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
