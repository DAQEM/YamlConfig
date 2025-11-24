package com.daqem.yamlconfig.impl.node;

import com.daqem.yamlconfig.api.node.IConfigNode;
import com.daqem.yamlconfig.api.node.IMapNode;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class ConfigMapNode extends BaseConfigNode implements IMapNode {

    // LinkedHashMap is crucial to preserve the order of keys in config files
    private final Map<String, IConfigNode> map = new LinkedHashMap<>();

    @Override
    public Map<String, IConfigNode> getValue() {
        return map;
    }

    @Override
    public void put(String key, IConfigNode node) {
        map.put(key, node);
    }

    @Override
    public IConfigNode get(String key) {
        return map.get(key);
    }

    @Override
    public boolean containsKey(String key) {
        return map.containsKey(key);
    }

    @Override
    public Set<Map.Entry<String, IConfigNode>> entrySet() {
        return map.entrySet();
    }

    @Override
    public void remove(String key) {
        map.remove(key);
    }
}