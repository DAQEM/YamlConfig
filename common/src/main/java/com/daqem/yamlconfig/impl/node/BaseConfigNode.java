package com.daqem.yamlconfig.impl.node;

import com.daqem.yamlconfig.api.node.IConfigNode;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseConfigNode implements IConfigNode {

    private List<String> comments = new ArrayList<>();

    @Override
    public List<String> getComments() {
        return comments;
    }

    @Override
    public void setComments(List<String> comments) {
        this.comments = comments != null ? new ArrayList<>(comments) : new ArrayList<>();
    }
}