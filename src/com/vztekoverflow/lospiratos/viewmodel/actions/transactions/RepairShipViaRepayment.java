package com.vztekoverflow.lospiratos.viewmodel.actions.transactions;

import com.vztekoverflow.lospiratos.viewmodel.actions.Action;
import com.vztekoverflow.lospiratos.viewmodel.actions.ActionIcon;

public class RepairShipViaRepayment extends RepairShip {
    @Override
    protected Action createCopyAndResetThis() {
        return new RepairShipViaRepayment();
    }

    @Override
    public void performOnShipInternal() {
        getRelatedShip().repairShip();
    }

    @Override
    public String getČeskéJméno() {
        return "oprava lodě pomocí zaplacení";
    }

    @Override
    protected void recomputeCost() {
        double coeff = 1 - getRelatedShip().getCurrentHP() / (double) getRelatedShip().getMaxHP();
        cost.setAll(getRelatedShip().getShipType().getBasicRepairCost().times(coeff));
    }

    @Override
    public ActionIcon getIcon() {
        return ActionIcon.repairViaPayment;
    }


}
