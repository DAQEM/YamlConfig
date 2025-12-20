package com.daqem.yamlconfig.api.config;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.daqem.yamlconfig.api.config.entry.IBooleanConfigEntry;
import com.daqem.yamlconfig.api.config.entry.IConfigEntry;
import com.daqem.yamlconfig.api.config.entry.IDateTimeConfigEntry;
import com.daqem.yamlconfig.api.config.entry.IEnumConfigEntry;
import com.daqem.yamlconfig.api.config.entry.IStackConfigEntry;
import com.daqem.yamlconfig.api.config.entry.IStringConfigEntry;
import com.daqem.yamlconfig.api.config.entry.list.IStringListConfigEntry;
import com.daqem.yamlconfig.api.config.entry.list.numeric.IDoubleListConfigEntry;
import com.daqem.yamlconfig.api.config.entry.list.numeric.IFloatListConfigEntry;
import com.daqem.yamlconfig.api.config.entry.list.numeric.IIntegerListConfigEntry;
import com.daqem.yamlconfig.api.config.entry.map.IStringMapConfigEntry;
import com.daqem.yamlconfig.api.config.entry.map.numeric.IDoubleMapConfigEntry;
import com.daqem.yamlconfig.api.config.entry.map.numeric.IFloatMapConfigEntry;
import com.daqem.yamlconfig.api.config.entry.map.numeric.IIntegerMapConfigEntry;
import com.daqem.yamlconfig.api.config.entry.minecraft.IRegistryConfigEntry;
import com.daqem.yamlconfig.api.config.entry.minecraft.IIdentifierConfigEntry;
import com.daqem.yamlconfig.api.config.entry.numeric.IDoubleConfigEntry;
import com.daqem.yamlconfig.api.config.entry.numeric.IFloatConfigEntry;
import com.daqem.yamlconfig.api.config.entry.numeric.IIntegerConfigEntry;
import com.daqem.yamlconfig.api.config.entry.numeric.ILongConfigEntry;

import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;

/**
 * Builds a configuration with various entry types.
 */
public interface IConfigBuilder {

    /**
     * Gets the mod ID associated with this builder.
     *
     * @return The mod ID.
     */
    String getModId();

    /**
     * Gets the name of the configuration being built.
     *
     * @return The configuration name.
     */
    String getName();

    /**
     * Gets the file extension for the configuration.
     *
     * @return The {@link ConfigExtension}.
     */
    ConfigExtension getExtension();

    /**
     * Gets the file path for the configuration.
     *
     * @return The {@link Path} to the configuration file.
     */
    Path getPath();

    /**
     * Checks if the configuration has already been built.
     *
     * @return True if built, false otherwise.
     */
    boolean isBuilt();

    /**
     * Builds the configuration.
     *
     * @return The built {@link IConfig}.
     */
    IConfig build();

    /**
     * Defines a custom configuration entry.
     *
     * @param entry The entry to define.
     * @param <T>   The type of the entry.
     * @return The defined entry.
     */
    <T extends IConfigEntry<?>> T define(T entry);

    /**
     * Defines a boolean configuration entry.
     *
     * @param key          The key for the entry.
     * @param defaultValue The default value.
     * @return The defined {@link IBooleanConfigEntry}.
     */
    IBooleanConfigEntry defineBoolean(String key, boolean defaultValue);

    /**
     * Defines an integer configuration entry.
     *
     * @param key          The key for the entry.
     * @param defaultValue The default value.
     * @return The defined {@link IIntegerConfigEntry}.
     */
    IIntegerConfigEntry defineInteger(String key, int defaultValue);

    IIntegerConfigEntry defineInteger(String key, int defaultValue, int minValue, int maxValue);

    ILongConfigEntry defineLong(String key, long defaultValue);

    ILongConfigEntry defineLong(String key, long defaultValue, long minValue, long maxValue);

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

    IIdentifierConfigEntry defineIdentifier(String key, Identifier defaultValue);

    IIdentifierConfigEntry defineIdentifier(String key, Identifier defaultValue, String pattern);

    /**
     * Pops the current context from the stack.
     */
    void pop();

    /**
     * Pushes a new context onto the stack.
     *
     * @param key The key for the new context.
     * @return The new {@link IStackConfigEntry} context.
     */
    IStackConfigEntry push(String key);
}
