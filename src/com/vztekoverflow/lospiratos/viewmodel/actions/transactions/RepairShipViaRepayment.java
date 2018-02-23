package com.vztekoverflow.lospiratos.viewmodel.actions.transactions;

import com.vztekoverflow.lospiratos.viewmodel.ResourceReadOnly;
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
        if(getRelatedShip().isDestroyed()){
            cost.setAll(getRelatedShip().getShipType().getBasicRepairCost());
            return;
        }
        double coeff = getRelatedShip().getCurrentHP() / (double) getRelatedShip().getMaxHP();
        ResourceReadOnly c = getRelatedShip().getShipType().getBuyingCost().times(1/50).times(1 + 4 * coeff);
        cost.setAll(c);
    }

    @Override
    public ActionIcon getIcon() {
        return ActionIcon.repairViaPayment;
    }


}
