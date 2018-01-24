package com.vztekoverflow.lospiratos.viewmodel.Actions;

public interface ParameterizedAction extends PlannableAction {
    Iterable<ActionParameter> getAvailableParameters();
}
