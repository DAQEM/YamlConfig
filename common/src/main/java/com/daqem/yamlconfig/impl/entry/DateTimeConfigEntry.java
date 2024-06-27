package com.daqem.yamlconfig.impl.entry;

import com.daqem.yamlconfig.api.IComments;
import com.daqem.yamlconfig.api.entry.IDateTimeConfigEntry;
import com.daqem.yamlconfig.api.exception.ConfigEntryValidationException;

import java.time.LocalDateTime;

public class DateTimeConfigEntry extends BaseConfigEntry<LocalDateTime> implements IDateTimeConfigEntry {

    private final LocalDateTime minDateTime;
    private final LocalDateTime maxDateTime;

    public DateTimeConfigEntry(String key, LocalDateTime defaultValue) {
        this(key, defaultValue, null, null);
    }

    public DateTimeConfigEntry(String key, LocalDateTime defaultValue, LocalDateTime minDateTime, LocalDateTime maxDateTime) {
        super(key, defaultValue);
        this.minDateTime = minDateTime;
        this.maxDateTime = maxDateTime;
    }

    @Override
    public void validate(LocalDateTime value) throws ConfigEntryValidationException {
        if ((minDateTime != null && value.isBefore(minDateTime)) || (maxDateTime != null && value.isAfter(maxDateTime))) {
            String minDateTime = this.minDateTime != null ? this.minDateTime.format(IDateTimeConfigEntry.DATE_TIME_FORMATTER) : null;
            String maxDateTime = this.maxDateTime != null ? this.maxDateTime.format(IDateTimeConfigEntry.DATE_TIME_FORMATTER) : null;
            throw new ConfigEntryValidationException(getKey(), "Value is out of bounds. Expected between " + minDateTime + " and " + maxDateTime);
        }
    }

    @Override
    public LocalDateTime getMinDateTime() {
        return minDateTime;
    }

    @Override
    public LocalDateTime getMaxDateTime() {
        return maxDateTime;
    }

    @Override
    public IComments getComments() {
        IComments comments = super.getComments();
        if (comments.showValidationParameters()) {
            if (minDateTime != null) {
                comments.addValidationParameter("Minimum value: " + minDateTime.format(IDateTimeConfigEntry.DATE_TIME_FORMATTER));
            }
            if (maxDateTime != null) {
                comments.addValidationParameter("Maximum value: " + maxDateTime.format(IDateTimeConfigEntry.DATE_TIME_FORMATTER));
            }
        }
        if (comments.showDefaultValues()) {
            comments.addDefaultValues(getDefaultValue().format(IDateTimeConfigEntry.DATE_TIME_FORMATTER));
        }
        return comments;
    }
}
