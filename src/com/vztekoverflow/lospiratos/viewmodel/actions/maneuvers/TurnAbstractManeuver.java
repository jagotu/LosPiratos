package com.vztekoverflow.lospiratos.viewmodel.actions.maneuvers;

import com.vztekoverflow.lospiratos.viewmodel.actions.Maneuver;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.Galleon;

abstract class TurnAbstractManeuver extends Maneuver {
    @Override
    protected boolean recomputePlannable() {
        boolean shipSpecificCondition = true;
        if(getRelatedShip().getShipType().getClass().equals(Galleon.class))
            shipSpecificCondition = shipHasPlannedExactly(0,TurnAbstractManeuver.class);
        return super.recomputePlannable() && shipSpecificCondition;
    }
}
