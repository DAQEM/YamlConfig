package com.daqem.yamlconfig.api.node;

import java.util.List;

public interface IListNode extends IConfigNode {
    @Override
    List<IConfigNode> getValue();

    void add(IConfigNode node);

    IConfigNode get(int index);

    int size();
}