package com.vztekoverflow.lospiratos.viewmodel.actions;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * This is a real plannable activity, useful for debugging or for a game master.
 * When planned, it allows any succeeding action to be planned, regardless of any limitations.
 * Moreover, when planned, then all other planned actions will be performed without any cost.
 */
public class ActivatePrivilegedMode extends Action {
    public static final BooleanProperty available = new SimpleBooleanProperty(true);

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
    protected Action createCopyAndResetThis() {
        return new ActivatePrivilegedMode();
    }

    @Override
    protected void recomputeCost() {

    }

    @Override
    protected void performOnShipInternal() {
        //void
    }

    @Override
    public ActionIcon getIcon() {
        return ActionIcon.priviledged;
    }

    @Override
    public String getČeskéJméno() {
        return "privilegovaný org";
    }
}
