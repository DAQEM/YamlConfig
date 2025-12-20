package com.daqem.yamlconfig.api.config.entry.minecraft;

import com.daqem.yamlconfig.api.config.entry.IConfigEntry;

import net.minecraft.resources.Identifier;

/**
 * Represents a resource location configuration entry.
 */
public interface IIdentifierConfigEntry extends IConfigEntry<Identifier> {
    /**
     * Gets the regex pattern the resource location must match.
     *
     * @return The regex pattern, or null if none.
     */
    String getPattern();
}
