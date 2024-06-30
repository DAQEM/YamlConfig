package com.daqem.yamlconfig.api.config.entry;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public interface IDateTimeConfigEntry extends IConfigEntry<LocalDateTime> {

    DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    LocalDateTime getMinDateTime();

    LocalDateTime getMaxDateTime();
}
