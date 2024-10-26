package com.daqem.yamlconfig.api.config.entry.list.numeric;

import com.daqem.yamlconfig.api.config.entry.list.IListConfigEntry;

public interface INumericListConfigEntry<T extends Number> extends IListConfigEntry<T> {

    T getMinValue();

    T getMaxValue();
}
