package com.daqem.yamlconfig.api.node;

import java.util.Map;
import java.util.Set;

public interface IMapNode extends IConfigNode {
    @Override
    Map<String, IConfigNode> getValue();

    void put(String key, IConfigNode node);
    IConfigNode get(String key);
    boolean containsKey(String key);
    Set<Map.Entry<String, IConfigNode>> entrySet();
    void remove(String key);
}