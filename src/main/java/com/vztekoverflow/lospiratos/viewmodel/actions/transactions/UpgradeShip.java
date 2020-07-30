package com.vztekoverflow.lospiratos.viewmodel.actions.transactions;

import com.vztekoverflow.lospiratos.viewmodel.actions.Action;
import com.vztekoverflow.lospiratos.viewmodel.actions.ActionIcon;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipType;

public class UpgradeShip extends ModifyShipTransaction {
    @Override
    protected Action createCopyAndResetThis() {
        return new UpgradeShip();
    }

    @Override
    protected boolean recomputePlannable() {
        return super.recomputePlannable() && ShipType.increment(getRelatedShip().getShipType().getClass()) != null
                && getRelatedShip().getTeam().getOwnedResource().isGreaterThanOrEqual(getCost());
    }

    @Override
    public void performOnShipInternal() {
        float HPRatio = getRelatedShip().getCurrentHP() / (float) getRelatedShip().getMaxHP();
        getRelatedShip().setShipType(ShipType.increment(getRelatedShip().getShipType().getClass()));
        getRelatedShip().setCurrentHP(getRelatedShip().getMaxHP());
    }

    @Override
    public String getČeskéJméno() {
        return "vylepšení typu lodi";
    }

    @Override
    protected void recomputeCost() {
        cost.setAll(getRelatedShip().getShipType().getUpgradeCost());
    }

    @Override
    public ActionIcon getIcon() {
        return ActionIcon.upgradeShip;
    }
}
