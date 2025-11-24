package com.daqem.yamlconfig.api.config.entry;

/**
 * Represents an enum configuration entry.
 *
 * @param <E> The enum type.
 */
public interface IEnumConfigEntry<E extends Enum<E>> extends IConfigEntry<E> {

    /**
     * Gets the class of the enum.
     *
     * @return The enum class.
     */
    Class<E> getEnumClass();
}
