package com.daqem.yamlconfig.event;

import com.daqem.yamlconfig.api.config.IConfig;
import net.minecraft.world.level.Level;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class ConfigEvent {

    private static final List<BiConsumer<IConfig, Level>> LISTENERS = new ArrayList<>();

    public static void register(BiConsumer<IConfig, Level> listener) {
        LISTENERS.add(listener);
    }

    public static void fireUpdate(IConfig config, Level level) {
        for (BiConsumer<IConfig, Level> listener : LISTENERS) {
            listener.accept(config, level);
        }
    }
}