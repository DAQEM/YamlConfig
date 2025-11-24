package com.daqem.yamlconfig.api.config;

import java.util.function.Supplier;

import com.daqem.yamlconfig.api.format.IConfigFormat;
import com.daqem.yamlconfig.impl.format.hocon.HoconFormat;
import com.daqem.yamlconfig.impl.format.json5.Json5Format;
import com.daqem.yamlconfig.impl.format.toml.TomlFormat;
import com.daqem.yamlconfig.impl.format.yaml.YamlFormat;

/**
 * Represents the supported file extensions for configuration files.
 */
public enum ConfigExtension {
    YAML(".yaml", YamlFormat::new),
    TOML(".toml", TomlFormat::new),
    JSON5(".json5", Json5Format::new),
    HOCON(".conf", HoconFormat::new);

    private final String extension;
    private final Supplier<IConfigFormat> formatSupplier;

    ConfigExtension(String extension, Supplier<IConfigFormat> formatSupplier) {
        this.extension = extension;
        this.formatSupplier = formatSupplier;
    }

    /**
     * Gets the file extension string.
     *
     * @return The file extension (e.g., ".yaml").
     */
    public String getExtension() {
        return extension;
    }

    /**
     * Creates a new instance of the {@link IConfigFormat} associated with this extension.
     *
     * @return A new {@link IConfigFormat} instance.
     */
    public IConfigFormat createFormat() {
        return formatSupplier.get();
    }
}