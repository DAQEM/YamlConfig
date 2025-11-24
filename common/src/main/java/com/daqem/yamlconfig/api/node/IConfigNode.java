package com.daqem.yamlconfig.api.node;

import java.util.List;

public interface IConfigNode {
    List<String> getComments();

    void setComments(List<String> comments);

    Object getValue();

    default <T> T getValue(Class<T> type) {
        return type.cast(getValue());
    }
}