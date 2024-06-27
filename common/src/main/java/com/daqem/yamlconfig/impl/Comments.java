package com.daqem.yamlconfig.impl;

import com.daqem.yamlconfig.api.IComments;

import java.util.ArrayList;
import java.util.List;

public class Comments implements IComments {

    private ArrayList<String> comments;
    private boolean showDefaultValues = true;
    private boolean showValidationParameters = true;
    private String validationParameters;

    public Comments(ArrayList<String> comments) {
        this.comments = comments;
    }

    @Override
    public List<String> getComments() {
        if (validationParameters != null) {
            comments.add(validationParameters);
        }
        return comments;
    }

    @Override
    public void setComments(ArrayList<String> comments) {
        this.comments = comments;
    }

    @Override
    public void addComment(String comment) {
        comments.add(comment);
    }

    @Override
    public boolean showDefaultValues() {
        return showDefaultValues;
    }

    @Override
    public void setShowDefaultValues(boolean showDefaultValues) {
        this.showDefaultValues = showDefaultValues;
    }

    @Override
    public boolean showValidationParameters() {
        return showValidationParameters;
    }

    @Override
    public void setShowValidationParameters(boolean showValidationParameters) {
        this.showValidationParameters = showValidationParameters;
    }

    @Override
    public String getValidationParameters() {
        return validationParameters;
    }

    @Override
    public void addValidationParameter(String parameter) {
        if (validationParameters == null) {
            validationParameters = parameter;
        } else {
            validationParameters += ", " + parameter;
        }
    }
}