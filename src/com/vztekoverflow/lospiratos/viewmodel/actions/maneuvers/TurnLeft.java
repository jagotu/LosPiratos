package com.vztekoverflow.lospiratos.viewmodel.actions.maneuvers;


import com.vztekoverflow.lospiratos.viewmodel.Position;
import com.vztekoverflow.lospiratos.viewmodel.actions.Action;
import com.vztekoverflow.lospiratos.viewmodel.actions.ActionIcon;

public class TurnLeft extends TurnAbstractManeuver {
    @Override
    protected Action createCopyAndResetThis() {
        return new TurnLeft();
    }

    @Override
    public void performOnTargetInternal() {
        performOn(getRelatedShip().getPosition());
    }

    @Override
    public String getČeskéJméno() {
        return "otočení vlevo";
    }

    @Override
    public void performOn(Position position) {
        position.rotateLeft();
    }

    @Override
    public ActionIcon getIcon() {
        return ActionIcon.turnLeft;
    }
}
