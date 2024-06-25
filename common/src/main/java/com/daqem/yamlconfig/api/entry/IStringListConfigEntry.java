package com.daqem.yamlconfig.api.entry;

import java.util.List;

public interface IStringListConfigEntry extends IListConfigEntry<String> {

    String getPattern();

    List<String> getValidValues();
}
