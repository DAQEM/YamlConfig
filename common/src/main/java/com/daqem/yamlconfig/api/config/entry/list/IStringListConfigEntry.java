package com.daqem.yamlconfig.api.config.entry.list;

import java.util.List;

public interface IStringListConfigEntry extends IListConfigEntry<String> {

    String getPattern();

    List<String> getValidValues();
}
