package com.vztekoverflow.lospiratos.viewmodel.actions.transactions;

import com.vztekoverflow.lospiratos.viewmodel.actions.Action;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipType;

public class UpgradeShip extends ChangeShipAbstractTransaction {
    @Override
    protected Action createCopyAndResetThis() {
        return new UpgradeShip();
    }

    @Override
    protected boolean recomputePlannable() {
        return super.recomputePlannable() && ShipType.increment(getRelatedShip().getShipType().getClass()) != null;
    }

    @Override
    public void performOnShipInternal() {
        getRelatedShip().setShipType(ShipType.increment(getRelatedShip().getShipType().getClass()));
    }

    @Override
    public String getČeskéJméno() {
        return "vylepšení typu lodi";
    }

    @Override
    protected void recomputeCost() {
        cost.setAll(getRelatedShip().getShipType().getUpgradeCost());
    }
}
