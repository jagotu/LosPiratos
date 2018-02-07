package com.vztekoverflow.lospiratos.viewmodel.actions;

import javafx.beans.value.ObservableBooleanValue;

public interface ParameterizedAction extends PlannableAction {
    Iterable<ActionParameter> getAvailableParameters();

    /**
     * Indicates whether all parameters are set and valid (i.e. their values are correct, e.g. in allowed range).
     */
    ObservableBooleanValue isSatisfied();
}
