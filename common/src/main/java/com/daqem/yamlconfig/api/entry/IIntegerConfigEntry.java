package com.daqem.yamlconfig.api.entry;

public interface IIntegerConfigEntry extends IConfigEntry<Integer> {

    int getMinValue();

    int getMaxValue();
}
