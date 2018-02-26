package com.vztekoverflow.lospiratos.viewmodel.actions.transactions;

import com.vztekoverflow.lospiratos.viewmodel.actions.Action;
import com.vztekoverflow.lospiratos.viewmodel.actions.Attack;
import com.vztekoverflow.lospiratos.viewmodel.actions.Maneuver;
import com.vztekoverflow.lospiratos.viewmodel.actions.Transaction;
import com.vztekoverflow.lospiratos.viewmodel.boardTiles.Port;

public abstract class ModifyShipTransaction extends Transaction {

    @Override
    public boolean preventsFromBeingPlanned(Action preventedAction) {
        return !(preventedAction instanceof ModifyShipTransaction);
    }

    @Override
    protected boolean recomputePlannable() {
        return super.recomputePlannable() &&
                getRelatedShip().getTeam().getGame().getBoard().getTiles().get(getRelatedShip().getCoordinate()) instanceof Port &&
                shipHasPlannedLessThan(1, Maneuver.class) &&
                shipHasPlannedLessThan(1, Attack.class);
    }

}
