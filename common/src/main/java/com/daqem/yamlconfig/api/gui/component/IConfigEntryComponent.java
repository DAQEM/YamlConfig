package com.daqem.yamlconfig.api.gui.component;

import com.daqem.uilib.api.component.IComponent;
import com.daqem.yamlconfig.api.config.entry.IConfigEntry;

/**
 * Represents a GUI component for a configuration entry.
 *
 * @param <C> The type of the configuration entry.
 */
public interface IConfigEntryComponent<C extends IConfigEntry<?>> extends IComponent {

    /**
     * Gets the configuration entry associated with this component.
     *
     * @return The {@link IConfigEntry}.
     */
    C getConfigEntry();

    /**
     * Applies the current value of the component to the configuration entry.
     */
    void applyValue();

    /**
     * Checks if the component has validation errors.
     *
     * @return True if there are validation errors, false otherwise.
     */
    boolean hasValidationErrors();
}
