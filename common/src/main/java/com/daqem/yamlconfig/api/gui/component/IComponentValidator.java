package com.daqem.yamlconfig.api.gui.component;

import java.util.List;

import net.minecraft.network.chat.Component;

/**
 * Functional interface for validating component values.
 */
@FunctionalInterface
public interface IComponentValidator {

    /**
     * Validates the given value.
     *
     * @param value The value to validate.
     * @return A list of error messages as {@link Component}s, or an empty list if valid.
     */
    List<Component> validate(String value);
}
