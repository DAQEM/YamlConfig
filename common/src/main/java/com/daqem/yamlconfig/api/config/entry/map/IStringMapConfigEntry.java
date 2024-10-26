package com.daqem.yamlconfig.api.config.entry.map;

import java.util.List;

public interface IStringMapConfigEntry extends IMapConfigEntry<String> {

    String getPattern();

    List<String> getValidValues();
}
