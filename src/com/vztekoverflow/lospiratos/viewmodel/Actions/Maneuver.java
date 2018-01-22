package com.vztekoverflow.lospiratos.viewmodel.Actions;

import com.vztekoverflow.lospiratos.viewmodel.Position;

public abstract class Maneuver extends Action {
    //this should still be overridden by concrete classes
    @Override
    public ActionIcon getIcon() {
        return ActionIcon.maneuverGenericIcon;
    }

    @Override
    protected boolean recomputePlannable() {
        int shipSpeed = getRelatedShip().getSpeed();
        return shipHasPlannedLessThan(shipSpeed, Maneuver.class);
    }
    @Override
    protected boolean recomputeVisible() {
        return true;
    }

    public abstract void performOn(Position position);
}
