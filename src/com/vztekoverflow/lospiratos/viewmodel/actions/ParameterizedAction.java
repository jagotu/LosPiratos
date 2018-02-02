package com.vztekoverflow.lospiratos.viewmodel.actions;

public interface ParameterizedAction extends PlannableAction {
    Iterable<ActionParameter> getAvailableParameters();
}
