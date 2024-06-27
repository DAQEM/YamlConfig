package com.daqem.yamlconfig.api.entry.list;

import com.daqem.yamlconfig.api.entry.IConfigEntry;

import java.util.List;

public interface IListConfigEntry<T> extends IConfigEntry<List<T>> {

    int getMinLength();

    int getMaxLength();
}
