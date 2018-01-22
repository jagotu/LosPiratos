package com.vztekoverflow.lospiratos.viewmodel.Actions;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class ActivatePrivilegedMode extends Action {
    public static final BooleanProperty available = new SimpleBooleanProperty();

    public ActivatePrivilegedMode() {
        available.addListener(__ -> visible.invalidate());
    }

    @Override
    protected boolean recomputeVisible() {
        return available.get();
    }

    @Override
    protected boolean recomputePlannable() {
        return true;
    }

    @Override
    protected Action createCopy() {
        return new ActivatePrivilegedMode();
    }

    @Override
    public void performOnTarget() {
        //void
    }

    @Override
    public ActionIcon getIcon() {
        return ActionIcon.priviledged;
    }

    @Override
    public String getČeskéJméno() {
        return "Privilegovaný org";
    }
}
