package com.daqem.yamlconfig.api.config.entry;

import java.util.List;

public interface IStringConfigEntry extends IConfigEntry<String> {

    int getMinLength();

    int getMaxLength();

    String getPattern();

    List<String> getValidValues();
}
