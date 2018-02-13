package com.vztekoverflow.lospiratos.viewmodel.actions;

import javafx.beans.binding.BooleanExpression;

public interface ParameterizedAction extends PlannableAction {
    Iterable<ActionParameter> getAvailableParameters();

    /**
     * Indicates whether all parameters are set and valid (i.e. their values are correct, e.g. in allowed range).
     */
    default boolean isSatisfied(){
        return satisfiedProperty().get();
    }

    /**
     * @return boolean expression indicating whether all parameters are set and valid (i.e. their values are correct, e.g. in allowed range).
     */
    BooleanExpression satisfiedProperty();
}
