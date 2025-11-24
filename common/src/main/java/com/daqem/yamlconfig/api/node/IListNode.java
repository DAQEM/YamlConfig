package com.daqem.yamlconfig.api.node;

import java.util.List;

/**
 * Represents a list node in the configuration tree.
 */
public interface IListNode extends IConfigNode {
    /**
     * Gets the list of child nodes.
     *
     * @return A list of {@link IConfigNode}s.
     */
    @Override
    List<IConfigNode> getValue();

    /**
     * Adds a child node to the list.
     *
     * @param node The node to add.
     */
    void add(IConfigNode node);

    /**
     * Gets the child node at the specified index.
     *
     * @param index The index of the child node.
     * @return The {@link IConfigNode} at the index.
     */
    IConfigNode get(int index);

    /**
     * Gets the number of child nodes in the list.
     *
     * @return The size of the list.
     */
    int size();
}