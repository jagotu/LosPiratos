package com.vztekoverflow.lospiratos.viewmodel.actions.transactions;

import com.vztekoverflow.lospiratos.viewmodel.actions.Action;
import com.vztekoverflow.lospiratos.viewmodel.actions.Transaction;

abstract public class ManeuverTransaction extends Transaction {
    @Override
    protected boolean recomputePlannable() {
        int shipSpeed = getRelatedShip().getSpeed();
        int maneuversAlreadyPlanned = getRelatedShip().getPlannedActions().stream().mapToInt(Action::getManeuverSlotsTaken).sum();
        return maneuversAlreadyPlanned < shipSpeed && super.recomputePlannable();
    }

    @Override
    public int getManeuverSlotsTaken() {
        return 1;
    }
}
