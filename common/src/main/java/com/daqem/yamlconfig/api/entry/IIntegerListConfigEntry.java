package com.daqem.yamlconfig.api.entry;

public interface IIntegerListConfigEntry extends IListConfigEntry<Integer> {

    int getMinValue();

    int getMaxValue();
}
