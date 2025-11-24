package com.daqem.yamlconfig.impl.node;

import com.daqem.yamlconfig.api.node.IConfigNode;
import com.daqem.yamlconfig.api.node.IListNode;

import java.util.ArrayList;
import java.util.List;

public class ConfigListNode extends BaseConfigNode implements IListNode {

    private final List<IConfigNode> list = new ArrayList<>();

    @Override
    public List<IConfigNode> getValue() {
        return list;
    }

    @Override
    public void add(IConfigNode node) {
        list.add(node);
    }

    @Override
    public IConfigNode get(int index) {
        return list.get(index);
    }

    @Override
    public int size() {
        return list.size();
    }
}