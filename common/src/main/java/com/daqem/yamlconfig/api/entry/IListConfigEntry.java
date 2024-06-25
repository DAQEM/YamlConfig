package com.daqem.yamlconfig.api.entry;

import java.util.List;

public interface IListConfigEntry<T> extends IConfigEntry<List<T>> {

    int getMinLength();

    int getMaxLength();
}
