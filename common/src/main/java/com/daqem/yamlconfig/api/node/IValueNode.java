package com.daqem.yamlconfig.api.node;

public interface IValueNode<T> extends IConfigNode {
    @Override
    T getValue();
    void setValue(T value);
}