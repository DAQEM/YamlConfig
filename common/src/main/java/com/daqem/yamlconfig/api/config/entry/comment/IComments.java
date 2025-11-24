package com.daqem.yamlconfig.api.config.entry.comment;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents comments associated with a configuration entry.
 */
public interface IComments {

    /**
     * Gets the list of comments.
     *
     * @return A list of comment strings.
     */
    List<String> getComments();

    /**
     * Gets the list of comments, optionally including validation parameters.
     *
     * @param showValidationParameters Whether to include validation parameters in the comments.
     * @return A list of comment strings.
     */
    List<String> getComments(boolean showValidationParameters);

    /**
     * Sets the comments.
     *
     * @param comments The list of comments to set.
     */
    void setComments(ArrayList<String> comments);

    /**
     * Adds a comment.
     *
     * @param comment The comment to add.
     */
    void addComment(String comment);

    /**
     * Checks if default values should be shown in the comments.
     *
     * @return True if default values should be shown, false otherwise.
     */
    boolean showDefaultValues();

    /**
     * Sets whether default values should be shown in the comments.
     *
     * @param showDefaultValues True to show default values, false otherwise.
     */
    void setShowDefaultValues(boolean showDefaultValues);

    /**
     * Checks if validation parameters should be shown in the comments.
     *
     * @return True if validation parameters should be shown, false otherwise.
     */
    boolean showValidationParameters();

    /**
     * Sets whether validation parameters should be shown in the comments.
     *
     * @param showValidationParameters True to show validation parameters, false otherwise.
     */
    void setShowValidationParameters(boolean showValidationParameters);

    /**
     * Gets the validation parameters string.
     *
     * @return The validation parameters string.
     */
    String getValidationParameters();

    /**
     * Adds a validation parameter.
     *
     * @param parameter The validation parameter to add.
     */
    void addValidationParameter(String parameter);

    /**
     * Adds a default value string to the comments.
     *
     * @param defaultValue The default value string to add.
     */
    void addDefaultValues(String defaultValue);

    /**
     * Resets the validation parameters.
     */
    void resetValidationParameters();
}
