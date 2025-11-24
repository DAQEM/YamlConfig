package com.daqem.yamlconfig.api.node;

/**
 * Represents a value node in the configuration tree.
 *
 * @param <T> The type of the value.
 */
public interface IValueNode<T> extends IConfigNode {
    /**
     * Gets the value stored in this node.
     *
     * @return The value.
     */
    @Override
    T getValue();

    /**
     * Sets the value for this node.
     *
     * @param value The value to set.
     */
    void setValue(T value);
}