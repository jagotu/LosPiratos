package com.vztekoverflow.lospiratos.viewmodel.actions;

import com.vztekoverflow.lospiratos.viewmodel.Board;
import com.vztekoverflow.lospiratos.viewmodel.BoardTile;
import com.vztekoverflow.lospiratos.viewmodel.Position;
import com.vztekoverflow.lospiratos.viewmodel.boardTiles.Port;

/**
 * Generic abstract user action that corresponds to economical transaction in a port.
 */
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

    protected boolean targetShipWillBeInAPort() {
        Position p = getRelatedShipsFuturePosition();
        Board b = getRelatedShip().getTeam().getGame().getBoard();
        BoardTile t = b.getTiles().get(p.getCoordinate());
        if (t == null) return false;
        return t.getClass().equals(Port.class);
    }

}
