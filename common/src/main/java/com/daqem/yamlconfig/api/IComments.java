package com.daqem.yamlconfig.api;

import java.util.ArrayList;
import java.util.List;

public interface IComments {

    List<String> getComments();

    void setComments(ArrayList<String> comments);

    void addComment(String comment);

    boolean showDefaultValues();

    void setShowDefaultValues(boolean showDefaultValues);

    boolean showValidationParameters();

    void setShowValidationParameters(boolean showValidationParameters);

    String getValidationParameters();

    void addValidationParameter(String parameter);
}
