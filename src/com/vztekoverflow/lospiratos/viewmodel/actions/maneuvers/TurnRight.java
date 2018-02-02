package com.vztekoverflow.lospiratos.viewmodel.actions.maneuvers;


import com.vztekoverflow.lospiratos.viewmodel.Position;
import com.vztekoverflow.lospiratos.viewmodel.actions.Action;
import com.vztekoverflow.lospiratos.viewmodel.actions.ActionIcon;

public class TurnRight extends TurnAbstractManeuver {
    @Override
    protected Action createCopy() {
        return new TurnRight();
    }

    @Override
    public void performOnTargetInternal() {
        performOn(getRelatedShip().getPosition());
    }

    @Override
    public String getČeskéJméno() {
        return "Otočení vpravo";
    }

    @Override
    public void performOn(Position position) {
        position.rotateRight();
    }

    @Override
    public ActionIcon getIcon() {
        return ActionIcon.turnRight;
    }
}
