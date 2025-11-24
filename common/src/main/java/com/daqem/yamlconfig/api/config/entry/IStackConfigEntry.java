package com.daqem.yamlconfig.api.config.entry;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents a stack configuration entry, which can contain nested entries.
 */
public interface IStackConfigEntry extends IConfigEntry<LinkedHashMap<String, IConfigEntry<?>>> {

    /**
     * Gets all flattened entries within this stack.
     *
     * @return A map of flattened keys to {@link IConfigEntry} instances.
     */
    default LinkedHashMap<String, IConfigEntry<?>> getEntries() {
        return getEntries("");
    }

    private LinkedHashMap<String, IConfigEntry<?>> getEntries(String previousKey) {
        return get().entrySet().stream()
                .collect(LinkedHashMap::new,
                        (entries, entry) -> {
                            String key = previousKey.isEmpty() ? entry.getKey() : previousKey + "." + entry.getKey();
                            if (entry.getValue() instanceof IStackConfigEntry stackConfigEntry) {
                                entries.putAll(stackConfigEntry.getEntries(key));
                            } else {
                                entries.put(key, entry.getValue());
                            }
                        }, Map::putAll);
    }
}
