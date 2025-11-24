package com.daqem.yamlconfig.api.config.entry;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a date-time configuration entry.
 */
public interface IDateTimeConfigEntry extends IConfigEntry<LocalDateTime> {

    /**
     * The default date-time formatter used for parsing and formatting.
     */
    DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Gets the minimum allowed date-time.
     *
     * @return The minimum {@link LocalDateTime}.
     */
    LocalDateTime getMinDateTime();

    /**
     * Gets the maximum allowed date-time.
     *
     * @return The maximum {@link LocalDateTime}.
     */
    LocalDateTime getMaxDateTime();
}
