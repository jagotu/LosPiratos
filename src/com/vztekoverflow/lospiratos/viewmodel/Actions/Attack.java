package com.vztekoverflow.lospiratos.viewmodel.Actions;

import com.vztekoverflow.lospiratos.viewmodel.ResourceReadOnly;

public abstract class Attack extends Action {

    //this should still be overridden by concrete classes
    @Override
    public ActionIcon getIcon() {
        return ActionIcon.attackGenericIcon;
    }

    @Override
    protected ResourceReadOnly recomputeCost() {
        return new ResourceReadOnly();
    }
}
