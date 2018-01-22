package com.vztekoverflow.lospiratos.viewmodel.Actions;

public abstract class Attack extends Action {

    //this should still be overridden by concrete classes
    @Override
    public ActionIcon getIcon() {
        return ActionIcon.attackGenericIcon;
    }
}
