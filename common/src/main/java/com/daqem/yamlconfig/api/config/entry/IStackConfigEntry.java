package com.daqem.yamlconfig.api.config.entry;

import java.util.HashMap;
import java.util.Map;

public interface IStackConfigEntry extends IConfigEntry<Map<String, IConfigEntry<?>>> {

    default Map<String, IConfigEntry<?>> getEntries() {
        return getEntries("");
    }

    private Map<String, IConfigEntry<?>> getEntries(String previousKey) {
        return get().entrySet().stream()
                .collect(HashMap::new,
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
