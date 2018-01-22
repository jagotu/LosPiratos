package com.vztekoverflow.lospiratos.viewmodel.Actions.Maneuvers ;


import com.vztekoverflow.lospiratos.viewmodel.Actions.Action;
import com.vztekoverflow.lospiratos.viewmodel.Position;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class TurnRight extends TurnAbstractManeuver {
    @Override
    protected Action createCopy() {
        return new TurnRight();
    }

    @Override
    public void performOnTarget() {
        throw new NotImplementedException();
    }

    @Override
    public String getČeskéJméno() {
        return "Otočení vpravo";
    }

    @Override
    public void performOn(Position position) {
        position.rotateRight();
    }
}
