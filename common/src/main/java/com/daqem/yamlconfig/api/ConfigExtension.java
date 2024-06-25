package com.daqem.yamlconfig.api;

public enum ConfigExtension {
    YAML(".yaml"),
    YML(".yml");

    private final String extension;

    ConfigExtension(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }
}
