package com.daqem.yamlconfig.api;

import com.daqem.yamlconfig.api.entry.*;
import com.daqem.yamlconfig.api.exception.YamlConfigException;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public interface IConfigBuilder {

    String getModId();

    String getName();

    ConfigExtension getExtension();

    Path getPath();

    boolean isBuilt();

    IConfig build();

    <T extends IConfigEntry<?>> T define(T entry);

    IBooleanConfigEntry defineBoolean(String key, boolean defaultValue);

    IIntegerConfigEntry defineInteger(String key, int defaultValue);

    IIntegerConfigEntry defineInteger(String key, int defaultValue, int minValue, int maxValue);

    IFloatConfigEntry defineFloat(String key, float defaultValue);

    IFloatConfigEntry defineFloat(String key, float defaultValue, float minValue, float maxValue);

    IDoubleConfigEntry defineDouble(String key, double defaultValue);

    IDoubleConfigEntry defineDouble(String key, double defaultValue, double minValue, double maxValue);

    IIntegerListConfigEntry defineIntegerList(String key, List<Integer> defaultValue);

    IIntegerListConfigEntry defineIntegerList(String key, List<Integer> defaultValue, int minValue, int maxValue);

    IIntegerListConfigEntry defineIntegerList(String key, List<Integer> defaultValue, int minValue, int maxValue, int minLength, int maxLength);

    IStringConfigEntry defineString(String key, String defaultValue);

    IStringConfigEntry defineString(String key, String defaultValue, int minLength, int maxLength);

    IStringConfigEntry defineString(String key, String defaultValue, int minLength, int maxLength, String pattern);

    IStringConfigEntry defineString(String key, String defaultValue, int minLength, int maxLength, List<String> validValues);

    IStringConfigEntry defineString(String key, String defaultValue, int minLength, int maxLength, String pattern, List<String> validValues);

    IStringListConfigEntry defineStringList(String key, List<String> defaultValue);

    IStringListConfigEntry defineStringList(String key, List<String> defaultValue, int minLength, int maxLength);

    IStringListConfigEntry defineStringList(String key, List<String> defaultValue, String pattern);

    IStringListConfigEntry defineStringList(String key, List<String> defaultValue, List<String> validValues);

    IStringListConfigEntry defineStringList(String key, List<String> defaultValue, String pattern, List<String> validValues);

    IStringListConfigEntry defineStringList(String key, List<String> defaultValue, int minLength, int maxLength, String pattern);

    IStringListConfigEntry defineStringList(String key, List<String> defaultValue, int minLength, int maxLength, List<String> validValues);
    IStringListConfigEntry defineStringList(String key, List<String> defaultValue, int minLength, int maxLength, String pattern, List<String> validValues);

    void pop();

    IStackConfigEntry push(String key);
}
