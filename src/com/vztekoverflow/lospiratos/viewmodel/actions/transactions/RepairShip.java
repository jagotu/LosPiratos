package com.vztekoverflow.lospiratos.viewmodel.actions.transactions;

public abstract class RepairShip extends ModifyShipTransaction {
    @Override
    protected boolean recomputePlannable() {
        return super.recomputePlannable() && getRelatedShip().getPlannedActions().size() == 0;
    }
}
