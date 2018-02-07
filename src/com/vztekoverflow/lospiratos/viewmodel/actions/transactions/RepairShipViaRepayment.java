package com.vztekoverflow.lospiratos.viewmodel.actions.transactions;

import com.vztekoverflow.lospiratos.viewmodel.ResourceReadOnly;
import com.vztekoverflow.lospiratos.viewmodel.actions.Action;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipType;

public class RepairShipViaRepayment extends RepairShip {
    @Override
    protected Action createCopyAndResetThis() {
        return new RepairShipViaRepayment();
    }

    @Override
    public void performOnTargetInternal() {
        getRelatedShip().repairShip();
    }

    @Override
    protected boolean recomputePlannable() {
        return super.recomputePlannable() && ShipType.decrement(getRelatedShip().getShipType().getClass()) != null;
    }

    @Override
    public String getČeskéJméno() {
        return "oprava lodě pomocí zaplacení";
    }

    @Override
    protected ResourceReadOnly recomputeCost() {
        //todo tady bych mel asi zapocitat i cenu za predchozi typy (napr. cenu za Skuner + za Brigu, jsem li Briga) - nebo ne?
        return getRelatedShip().getShipType().getCostUniversal().times(repairCostCoefficient);
    }

    static final double repairCostCoefficient = 0.1;
}
