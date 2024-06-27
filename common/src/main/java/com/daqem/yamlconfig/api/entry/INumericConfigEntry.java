package com.daqem.yamlconfig.api.entry;

public interface INumericConfigEntry<T extends Number & Comparable<T>> extends IConfigEntry<T> {

    T getMinValue();

    T getMaxValue();
}
