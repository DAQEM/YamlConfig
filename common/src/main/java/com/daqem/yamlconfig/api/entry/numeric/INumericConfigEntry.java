package com.daqem.yamlconfig.api.entry.numeric;

import com.daqem.yamlconfig.api.entry.IConfigEntry;

public interface INumericConfigEntry<T extends Number & Comparable<T>> extends IConfigEntry<T> {

    T getMinValue();

    T getMaxValue();
}
