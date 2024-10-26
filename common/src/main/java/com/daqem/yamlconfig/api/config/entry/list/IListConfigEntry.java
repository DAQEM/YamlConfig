package com.daqem.yamlconfig.api.config.entry.list;

import com.daqem.yamlconfig.api.config.entry.IConfigEntry;

import java.util.List;

public interface IListConfigEntry<T> extends IConfigEntry<List<T>> {

    int getMinLength();

    int getMaxLength();
}
