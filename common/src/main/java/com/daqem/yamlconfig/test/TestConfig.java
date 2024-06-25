package com.daqem.yamlconfig.test;

import com.daqem.yamlconfig.api.ConfigExtension;
import com.daqem.yamlconfig.impl.ConfigBuilder;
import com.daqem.yamlconfig.impl.entry.IntegerConfigEntry;
import com.daqem.yamlconfig.impl.entry.IntegerListConfigEntry;
import com.daqem.yamlconfig.impl.entry.StringConfigEntry;
import com.daqem.yamlconfig.impl.entry.StringListConfigEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestConfig {

    public static IntegerConfigEntry testInt;
    public static StringConfigEntry testString;
    public static StringConfigEntry testString1;
    public static StringListConfigEntry testStringList;
    public static IntegerListConfigEntry testIntList;

    public static void init() {
        ConfigBuilder builder = new ConfigBuilder("test", "test-common", ConfigExtension.YAML);
        testInt = builder.define(new IntegerConfigEntry("testInt", 10, 0, 100));
        testString = builder.define(new StringConfigEntry("testString", "test"));
        builder.push("test");
        testString1 = builder.define(new StringConfigEntry("testString", "test"));
        builder.pop();
        testStringList = builder.define(new StringListConfigEntry("testStringList", List.of("test1", "test2", "test3")));
        testIntList = builder.define(new IntegerListConfigEntry("testIntList", List.of(1, 2, 3), 1, 3));
        builder.build();
    }
}
