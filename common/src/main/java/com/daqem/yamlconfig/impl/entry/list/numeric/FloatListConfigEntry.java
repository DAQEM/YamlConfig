package com.daqem.yamlconfig.impl.entry.list.numeric;

import com.daqem.yamlconfig.api.entry.list.numeric.IFloatListConfigEntry;

import java.util.List;

public class FloatListConfigEntry extends BaseNumericListConfigEntry<Float> implements IFloatListConfigEntry {

    public FloatListConfigEntry(String key, List<Float> value) {
        super(key, value);
    }

    public FloatListConfigEntry(String key, List<Float> value, int minLength, int maxLength) {
        super(key, value, minLength, maxLength);
    }

    public FloatListConfigEntry(String key, List<Float> value, int minLength, int maxLength, Float minValue, Float maxValue) {
        super(key, value, minLength, maxLength, minValue, maxValue);
    }
}
