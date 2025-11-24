package com.daqem.yamlconfig.impl.node;

import com.daqem.yamlconfig.api.node.IValueNode;

public class ConfigValueNode<T> extends BaseConfigNode implements IValueNode<T> {

    private T value;

    public ConfigValueNode(T value) {
        this.value = value;
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}