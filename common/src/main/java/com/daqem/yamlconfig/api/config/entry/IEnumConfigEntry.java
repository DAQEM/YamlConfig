package com.daqem.yamlconfig.api.config.entry;

public interface IEnumConfigEntry<E extends Enum<E>> extends IConfigEntry<E> {

    Class<E> getEnumClass();
}
