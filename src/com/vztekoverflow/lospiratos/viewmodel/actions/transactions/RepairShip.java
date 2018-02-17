package com.vztekoverflow.lospiratos.viewmodel.actions.transactions;

abstract class RepairShip extends ChangeShipAbstractTransaction {
    @Override
    protected boolean recomputePlannable() {
        return super.recomputePlannable() && getRelatedShip().getPlannedActions().size() == 0;
    }
}
