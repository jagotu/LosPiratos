package com.vztekoverflow.lospiratos.viewmodel.actions.maneuvers;

import com.vztekoverflow.lospiratos.viewmodel.Position;
import com.vztekoverflow.lospiratos.viewmodel.actions.Action;
import com.vztekoverflow.lospiratos.viewmodel.actions.ActionIcon;
import com.vztekoverflow.lospiratos.viewmodel.actions.Maneuver;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.Frigate;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.Galleon;

public class MoveForward extends Maneuver {

    @Override
    protected Action createCopyAndResetThis() {
        return new MoveForward();
    }

    @Override
    protected boolean recomputePlannable() {
        boolean shipSpecificCondition = true;
        if (getRelatedShip().getShipType().getClass().equals(Frigate.class))
            shipSpecificCondition = shipHasPlannedLessThan(2, MoveForward.class);
        if (getRelatedShip().getShipType().getClass().equals(Galleon.class))
            shipSpecificCondition = shipHasPlannedLessThan(1, MoveForward.class);
        Position futurePosition = getRelatedShipsFuturePosition();
        performOn(futurePosition);
        return super.recomputePlannable() && shipSpecificCondition &&
                getRelatedShip().getTeam().getGame().getBoard().canStepOn(futurePosition.getCoordinate());

    }

    @Override
    public void performOnShipInternal() {
        Position futurePosition = getRelatedShip().getPosition().createCopy();
        performOn(futurePosition);
        if(getRelatedShip().getTeam().getGame().getBoard().canStepOn(futurePosition.getCoordinate()))
            performOn(getRelatedShip().getPosition());
        else
            getEventLogger().logActionNotPerformed(this,"na políčko " + futurePosition.getCoordinate() + " nelze vstoupit");
    }

    @Override
    public String getČeskéJméno() {
        return "pohyb kupředu";
    }

    @Override
    public void performOn(Position position) {
        position.moveForward();
    }

    @Override
    public void undo() {
        Position p = getRelatedShip().getPosition();
        p.rotateRight();
        p.rotateRight();
        p.rotateRight();
        p.moveForward();
        p.rotateLeft();
        p.rotateLeft();
        p.rotateLeft();
    }

    @Override
    public ActionIcon getIcon() {
        return ActionIcon.moveForward;
    }
}
