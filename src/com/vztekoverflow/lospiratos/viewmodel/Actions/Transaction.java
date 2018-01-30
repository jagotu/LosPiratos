package com.vztekoverflow.lospiratos.viewmodel.Actions;

import com.vztekoverflow.lospiratos.viewmodel.Board;
import com.vztekoverflow.lospiratos.viewmodel.BoardTiles.Port;
import com.vztekoverflow.lospiratos.viewmodel.Position;

public abstract class Transaction extends Action {

    //this should still be overridden by concrete classes
    @Override
    public ActionIcon getIcon() {
        return ActionIcon.maneuverGenericIcon;
    }

    @Override
    protected boolean recomputeVisible() {
        return true;
    }

    @Override
    protected boolean recomputePlannable() {
        return targetShipWillBeInAPort();
    }

    protected boolean targetShipWillBeInAPort(){
        Position p = getRelatedShip().getPosition().createCopy();
        getRelatedShip().getPlannedActions().stream().
                filter(a -> Maneuver.class.isAssignableFrom(a.getClass())).
                map( a -> (Maneuver) a).forEach( a -> a.performOn(p));
        Board b = getRelatedShip().getTeam().getGame().getBoard();
        return  b.getTiles().get(p.getCoordinate()).getClass().equals(Port.class);
    }

}
