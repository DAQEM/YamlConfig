package com.daqem.yamlconfig.api.config.entry;

import java.util.function.Supplier;

import com.daqem.yamlconfig.api.config.entry.comment.IComments;
import com.daqem.yamlconfig.api.config.entry.type.IConfigEntryType;
import com.daqem.yamlconfig.api.exception.ConfigEntryValidationException;

/**
 * Represents a configuration entry.
 *
 * @param <T> The type of the value stored in the entry.
 */
public interface IConfigEntry<T> extends Supplier<T> {

    /**
     * Gets the key of the configuration entry.
     *
     * @return The key.
     */
    String getKey();

    /**
     * Gets the default value of the configuration entry.
     *
     * @return The default value.
     */
    T getDefaultValue();

    /**
     * Sets the value of the configuration entry.
     *
     * @param value The value to set.
     */
    void set(T value);

    /**
     * Gets the comments associated with the configuration entry.
     *
     * @return The {@link IComments} object.
     */
    IComments getComments();

    /**
     * Sets the comments for the configuration entry.
     *
     * @param comments The comments to set.
     * @return The {@link IConfigEntry} instance.
     */
    IConfigEntry<T> withComments(String... comments);

    /**
     * Sets the comments for the configuration entry, optionally showing default values.
     *
     * @param showDefaultValues Whether to show default values in the comments.
     * @param comments          The comments to set.
     * @return The {@link IConfigEntry} instance.
     */
    IConfigEntry<T> withComments(boolean showDefaultValues, String... comments);

    /**
     * Sets the comments for the configuration entry, optionally showing default values and validation parameters.
     *
     * @param showDefaultValues        Whether to show default values in the comments.
     * @param showValidationParameters Whether to show validation parameters in the comments.
     * @param comments                 The comments to set.
     * @return The {@link IConfigEntry} instance.
     */
    IConfigEntry<T> withComments(boolean showDefaultValues, boolean showValidationParameters, String... comments);

    /**
     * Validates the provided value against the entry's constraints.
     *
     * @param value The value to validate.
     * @throws ConfigEntryValidationException If the value is invalid.
     */
    void validate(T value) throws ConfigEntryValidationException;

    /**
     * Gets the type of the configuration entry.
     *
     * @return The {@link IConfigEntryType}.
     */
    IConfigEntryType<IConfigEntry<T>, T> getType();

    /**
     * Checks if the entry should be synced to the client.
     *
     * @return True if it should be synced, false otherwise.
     */
    boolean shouldBeSynced();

    /**
     * Marks the entry as not to be synced.
     *
     * @return The {@link IConfigEntry} instance.
     */
    IConfigEntry<T> dontSync();

    /**
     * Sets whether the entry should be synced.
     *
     * @param shouldBeSynced True to sync, false otherwise.
     * @return The {@link IConfigEntry} instance.
     */
    IConfigEntry<T> setShouldBeSynced(boolean shouldBeSynced);
}
