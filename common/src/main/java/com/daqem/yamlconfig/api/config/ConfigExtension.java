package com.daqem.yamlconfig.api.config;

import com.daqem.yamlconfig.api.format.IConfigFormat;
import com.daqem.yamlconfig.impl.format.hocon.HoconFormat;
import com.daqem.yamlconfig.impl.format.json5.Json5Format;
import com.daqem.yamlconfig.impl.format.toml.TomlFormat;
import com.daqem.yamlconfig.impl.format.yaml.YamlFormat;

import java.util.function.Supplier;

public enum ConfigExtension {
    YAML(".yaml", YamlFormat::new),
    YML(".yml", YamlFormat::new),
    TOML(".toml", TomlFormat::new),
    JSON5(".json5", Json5Format::new),
    HOCON(".conf", HoconFormat::new);

    private final String extension;
    private final Supplier<IConfigFormat> formatSupplier;

    ConfigExtension(String extension, Supplier<IConfigFormat> formatSupplier) {
        this.extension = extension;
        this.formatSupplier = formatSupplier;
    }

    public String getExtension() {
        return extension;
    }

    public IConfigFormat createFormat() {
        return formatSupplier.get();
    }
}