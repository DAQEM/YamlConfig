package com.daqem.yamlconfig.api;

import com.daqem.yamlconfig.api.entry.*;
import com.daqem.yamlconfig.api.entry.list.IStringListConfigEntry;
import com.daqem.yamlconfig.api.entry.list.numeric.IDoubleListConfigEntry;
import com.daqem.yamlconfig.api.entry.list.numeric.IFloatListConfigEntry;
import com.daqem.yamlconfig.api.entry.list.numeric.IIntegerListConfigEntry;
import com.daqem.yamlconfig.api.entry.map.IStringMapConfigEntry;
import com.daqem.yamlconfig.api.entry.map.numeric.IDoubleMapConfigEntry;
import com.daqem.yamlconfig.api.entry.map.numeric.IFloatMapConfigEntry;
import com.daqem.yamlconfig.api.entry.map.numeric.IIntegerMapConfigEntry;
import com.daqem.yamlconfig.api.entry.minecraft.IRegistryConfigEntry;
import com.daqem.yamlconfig.api.entry.numeric.IDoubleConfigEntry;
import com.daqem.yamlconfig.api.entry.numeric.IFloatConfigEntry;
import com.daqem.yamlconfig.api.entry.numeric.IIntegerConfigEntry;
import net.minecraft.core.Registry;

import java.nio.file.Path;
import java.time.LocalDateTime;
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

    IIntegerListConfigEntry defineIntegerList(String key, List<Integer> defaultValue, int minLength, int maxLength);

    IIntegerListConfigEntry defineIntegerList(String key, List<Integer> defaultValue, int minLength, int maxLength, int minValue, int maxValue);

    IFloatListConfigEntry defineFloatList(String key, List<Float> defaultValue);

    IFloatListConfigEntry defineFloatList(String key, List<Float> defaultValue, int minLength, int maxLength);

    IFloatListConfigEntry defineFloatList(String key, List<Float> defaultValue, int minLength, int maxLength, float minValue, float maxValue);

    IDoubleListConfigEntry defineDoubleList(String key, List<Double> defaultValue);

    IDoubleListConfigEntry defineDoubleList(String key, List<Double> defaultValue, int minLength, int maxLength);

    IDoubleListConfigEntry defineDoubleList(String key, List<Double> defaultValue, int minLength, int maxLength, double minValue, double maxValue);

    IIntegerMapConfigEntry defineIntegerMap(String key, Map<String, Integer> defaultValue);

    IIntegerMapConfigEntry defineIntegerMap(String key, Map<String, Integer> defaultValue, int minLength, int maxLength);

    IIntegerMapConfigEntry defineIntegerMap(String key, Map<String, Integer> defaultValue, int minLength, int maxLength, int minValue, int maxValue);

    IFloatMapConfigEntry defineFloatMap(String key, Map<String, Float> defaultValue);

    IFloatMapConfigEntry defineFloatMap(String key, Map<String, Float> defaultValue, int minLength, int maxLength);

    IFloatMapConfigEntry defineFloatMap(String key, Map<String, Float> defaultValue, int minLength, int maxLength, float minValue, float maxValue);

    IDoubleMapConfigEntry defineDoubleMap(String key, Map<String, Double> defaultValue);

    IDoubleMapConfigEntry defineDoubleMap(String key, Map<String, Double> defaultValue, int minLength, int maxLength);

    IDoubleMapConfigEntry defineDoubleMap(String key, Map<String, Double> defaultValue, int minLength, int maxLength, double minValue, double maxValue);

    <E extends Enum<E>> IEnumConfigEntry<E> defineEnum(String key, E defaultValue, Class<E> enumClass);

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

    IStringMapConfigEntry defineStringMap(String key, Map<String, String> defaultValue);

    IStringMapConfigEntry defineStringMap(String key, Map<String, String> defaultValue, int minLength, int maxLength);

    IStringMapConfigEntry defineStringMap(String key, Map<String, String> defaultValue, int minLength, int maxLength, String pattern);

    IStringMapConfigEntry defineStringMap(String key, Map<String, String> defaultValue, int minLength, int maxLength, List<String> validValues);

    IStringMapConfigEntry defineStringMap(String key, Map<String, String> defaultValue, int minLength, int maxLength, String pattern, List<String> validValues);

    IDateTimeConfigEntry defineDateTime(String key, LocalDateTime defaultValue);

    IDateTimeConfigEntry defineDateTime(String key, LocalDateTime defaultValue, LocalDateTime minDateTime, LocalDateTime maxDateTime);

    <T> IRegistryConfigEntry<T> defineRegistry(String key, T defaultValue, Registry<T> registry);

    void pop();

    IStackConfigEntry push(String key);
}
