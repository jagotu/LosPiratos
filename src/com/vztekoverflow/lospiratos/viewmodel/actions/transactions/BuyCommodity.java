package com.vztekoverflow.lospiratos.viewmodel.actions.transactions;

import com.vztekoverflow.lospiratos.viewmodel.actions.Action;
import com.vztekoverflow.lospiratos.viewmodel.ResourceReadOnly;

public class BuyCommodity extends CommodityTransaction {

    @Override
    protected Action createCopy() {
        return new BuyCommodity();
    }

    @Override
    public void performOnTargetInternal() {
        //price has already been paid by the caller
        getRelatedShip().getTeam().getOwnedResource().add(this.getCommodities());
    }

    @Override
    public String getČeskéJméno() {
        return "nákup komodit";
    }

    @Override
    protected ResourceReadOnly recomputeCost() {
        return ResourceReadOnly.fromMoney(getCommodities().scalarProduct(purchaseCoefficients));
    }

    //todo opravdickou hodnotu
    static final ResourceReadOnly purchaseCoefficients = new ResourceReadOnly (1,2,2,2,2,2);
}
