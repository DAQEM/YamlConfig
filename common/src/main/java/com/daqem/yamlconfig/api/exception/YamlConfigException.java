package com.daqem.yamlconfig.api.exception;

public class YamlConfigException extends RuntimeException {

    public YamlConfigException(String message) {
        super(message);
    }

    public YamlConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}
