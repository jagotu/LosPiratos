package com.vztekoverflow.lospiratos.viewmodel.actions;

import com.vztekoverflow.lospiratos.viewmodel.Position;

/**
 * Generic abstract user action that may move the ship it is performed on.
 */
public abstract class Maneuver extends Action {
    //this should still be overridden by concrete classes
    @Override
    public ActionIcon getIcon() {
        return ActionIcon.maneuverGenericIcon;
    }

    @Override
    protected boolean recomputePlannable() {
        int shipSpeed = getRelatedShip().getSpeed();
        int maneuversAlreadyPlanned = getRelatedShip().getPlannedActions().stream().mapToInt(Action::getManeuverSlotsTaken).sum();
        return maneuversAlreadyPlanned < shipSpeed;
    }

    @Override
    protected boolean recomputeVisible() {
        return true;
    }

    public abstract void performOn(Position position);

    public abstract void undo();

    @Override
    public int getManeuverSlotsTaken() {
        return 1;
    }

    @Override
    protected void recomputeCost() {
        //do nothing, this actions costs nothing
    }
}
