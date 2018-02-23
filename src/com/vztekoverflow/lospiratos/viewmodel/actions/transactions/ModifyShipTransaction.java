package com.vztekoverflow.lospiratos.viewmodel.actions.transactions;

import com.vztekoverflow.lospiratos.viewmodel.actions.Attack;
import com.vztekoverflow.lospiratos.viewmodel.actions.Maneuver;
import com.vztekoverflow.lospiratos.viewmodel.actions.Transaction;
import com.vztekoverflow.lospiratos.viewmodel.boardTiles.Port;

public abstract class ModifyShipTransaction extends Transaction {

    //toto je verze podle starych pravidel; nechavam ji tu, kdyby se pravidla zase menila
    //    @Override
//    public boolean preventsFromBeingPlanned(Action preventedAction) {
//        return Maneuver.class.isAssignableFrom(preventedAction.getClass()) ||
//                Attack.class.isAssignableFrom(preventedAction.getClass());
//    }

    @Override
    protected boolean recomputePlannable() {
        return super.recomputePlannable() &&
                getRelatedShip().getTeam().getGame().getBoard().getTiles().get(getRelatedShip().getCoordinate()) instanceof Port &&
                shipHasPlannedLessThan(1, Maneuver.class) &&
                shipHasPlannedLessThan(1, Attack.class);
    }

}
