package com.daqem.yamlconfig.impl.config;

import com.daqem.yamlconfig.api.config.ConfigExtension;
import com.daqem.yamlconfig.api.config.ConfigType;
import com.daqem.yamlconfig.api.config.entry.IStackConfigEntry;

import java.nio.file.Path;

public class CommonConfig extends BaseConfig {

    public CommonConfig(String modId, String name, ConfigExtension extension, Path path, IStackConfigEntry context) {
        super(modId, name, extension, ConfigType.COMMON, path, context);
    }
}
