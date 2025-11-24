package com.daqem.yamlconfig.client.gui.registry;

import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.config.entry.type.IConfigEntryType;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ConfigEntryComponentRegistry {

    private static final Map<IConfigEntryType<?, ?>, IConfigEntryComponentFactory<?>> FACTORIES = new HashMap<>();

    public static <C extends IConfigEntry<?>> void register(IConfigEntryType<C, ?> type, IConfigEntryComponentFactory<C> factory) {
        FACTORIES.put(type, factory);
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public static <C extends IConfigEntry<?>> IConfigEntryComponentFactory<C> getFactory(IConfigEntryType<C, ?> type) {
        return (IConfigEntryComponentFactory<C>) FACTORIES.get(type);
    }
}