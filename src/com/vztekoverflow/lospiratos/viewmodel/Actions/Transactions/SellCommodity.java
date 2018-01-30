package com.vztekoverflow.lospiratos.viewmodel.Actions.Transactions ;

import com.vztekoverflow.lospiratos.viewmodel.Actions.Action;
import com.vztekoverflow.lospiratos.viewmodel.ResourceReadOnly;

public class SellCommodity extends CommodityTransaction {

    @Override
    protected Action createCopy() {
        return new SellCommodity();
    }

    @Override
    public void performOnTargetInternal() {
        //price (negative value, ie. gain) has already been paid by the caller
        getRelatedShip().getTeam().getOwnedResource().subtract(this.getCommodities());
    }

    @Override
    public String getČeskéJméno() {
        return "prodej komodit";
    }

    @Override
    protected ResourceReadOnly recomputeCost() {
        return ResourceReadOnly.fromMoney(getCommodities().scalarProduct(sellCoefficients) * -1);
        //return negative value, because this cost is actually a gain (we are selling something)
    }

    //todo opravdickou hodnotu
    static final ResourceReadOnly sellCoefficients = new ResourceReadOnly (1,2,2,2,2,2);
}
