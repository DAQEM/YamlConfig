package com.daqem.yamlconfig.api.node;

import java.util.List;

/**
 * Represents a node in the configuration tree.
 */
public interface IConfigNode {
    /**
     * Gets the comments associated with this node.
     *
     * @return A list of comments.
     */
    List<String> getComments();

    /**
     * Sets the comments for this node.
     *
     * @param comments The list of comments to set.
     */
    void setComments(List<String> comments);

    /**
     * Gets the value stored in this node.
     *
     * @return The value.
     */
    Object getValue();

    /**
     * Gets the value stored in this node, cast to the specified type.
     *
     * @param type The class of the type to cast to.
     * @param <T>  The type to cast to.
     * @return The cast value.
     */
    default <T> T getValue(Class<T> type) {
        return type.cast(getValue());
    }
}