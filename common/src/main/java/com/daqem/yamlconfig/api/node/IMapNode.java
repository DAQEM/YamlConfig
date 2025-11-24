package com.daqem.yamlconfig.api.node;

import java.util.Map;
import java.util.Set;

/**
 * Represents a map node in the configuration tree.
 */
public interface IMapNode extends IConfigNode {
    /**
     * Gets the map of child nodes.
     *
     * @return A map of keys to {@link IConfigNode}s.
     */
    @Override
    Map<String, IConfigNode> getValue();

    /**
     * Puts a child node into the map.
     *
     * @param key  The key for the node.
     * @param node The node to put.
     */
    void put(String key, IConfigNode node);

    /**
     * Gets a child node by key.
     *
     * @param key The key of the node.
     * @return The {@link IConfigNode}, or null if not found.
     */
    IConfigNode get(String key);

    /**
     * Checks if the map contains the specified key.
     *
     * @param key The key to check.
     * @return True if the key exists, false otherwise.
     */
    boolean containsKey(String key);

    /**
     * Gets the set of entries in the map.
     *
     * @return A set of map entries.
     */
    Set<Map.Entry<String, IConfigNode>> entrySet();

    /**
     * Removes a child node by key.
     *
     * @param key The key of the node to remove.
     */
    void remove(String key);
}