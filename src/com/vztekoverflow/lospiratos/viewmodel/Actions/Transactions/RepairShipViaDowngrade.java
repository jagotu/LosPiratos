package com.vztekoverflow.lospiratos.viewmodel.Actions.Transactions;

import com.vztekoverflow.lospiratos.util.Warnings;
import com.vztekoverflow.lospiratos.viewmodel.Actions.Action;
import com.vztekoverflow.lospiratos.viewmodel.ResourceReadOnly;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipType;

public class RepairShipViaDowngrade extends RepairShip {
    @Override
    protected Action createCopy() {
        return new RepairShipViaDowngrade();
    }

    @Override
    public void performOnTargetInternal() {
        Class<? extends ShipType> newType = ShipType.decrement(getRelatedShip().getShipType().getClass());
        if(newType == null){
            Warnings.makeWarning(toString()+"perform()", "Attempt to downgrade a ship that is not downgradable: " + getRelatedShip());
            return;
        }
        getRelatedShip().repairShip();
        getRelatedShip().setShipType(newType);
    }

    @Override
    public String getČeskéJméno() {
        return "oprava lodě pomocí downgrade";
    }

    @Override
    protected ResourceReadOnly recomputeCost() {
        return ResourceReadOnly.ZERO;
    }
}
