package com.vztekoverflow.lospiratos.viewmodel.actions.transactions;

import com.vztekoverflow.lospiratos.util.Warnings;
import com.vztekoverflow.lospiratos.viewmodel.actions.Action;
import com.vztekoverflow.lospiratos.viewmodel.actions.ActionIcon;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipType;

public class RepairShipViaDowngrade extends RepairShip {
    @Override
    protected Action createCopyAndResetThis() {
        return new RepairShipViaDowngrade();
    }

    @Override
    public void performOnShipInternal() {
        Class<? extends ShipType> newType = ShipType.decrement(getRelatedShip().getShipType().getClass());
        if (newType == null) {
            Warnings.makeWarning(toString() + "perform()", "Attempt to downgrade a ship that is not downgradable: " + getRelatedShip());
            return;
        }
        getRelatedShip().setShipType(newType);
        getRelatedShip().repairShip();
    }

    @Override
    protected boolean recomputePlannable() {
        return super.recomputePlannable() && ShipType.decrement(getRelatedShip().getShipType().getClass()) != null;
    }

    @Override
    public String getČeskéJméno() {
        return "oprava lodě pomocí downgrade";
    }

    @Override
    protected void recomputeCost() {
        //nothing
    }

    @Override
    public ActionIcon getIcon() {
        return ActionIcon.repairViaDowngrade;
    }
}
