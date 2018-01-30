package com.vztekoverflow.lospiratos.viewmodel.Actions.Maneuvers ;


import com.vztekoverflow.lospiratos.viewmodel.Actions.Action;
import com.vztekoverflow.lospiratos.viewmodel.Position;

public class TurnLeft extends TurnAbstractManeuver {
    @Override
    protected Action createCopy() {
        return new TurnLeft();
    }

    @Override
    public void performOnTargetInternal() {
        performOn(getRelatedShip().getPosition());
    }

    @Override
    public String getČeskéJméno() {
        return "Otočení vlevo";
    }

    @Override
    public void performOn(Position position) {
        position.rotateLeft();
    }
}
