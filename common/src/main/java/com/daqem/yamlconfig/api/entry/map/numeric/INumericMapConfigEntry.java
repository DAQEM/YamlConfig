package com.daqem.yamlconfig.api.entry.map.numeric;

import com.daqem.yamlconfig.api.entry.map.IMapConfigEntry;

public interface INumericMapConfigEntry<T extends Number & Comparable<T>> extends IMapConfigEntry<T> {

    T getMinValue();

    T getMaxValue();
}
