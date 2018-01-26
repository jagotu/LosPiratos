package com.vztekoverflow.lospiratos.viewmodel.Actions.Transactions ;

import com.vztekoverflow.lospiratos.viewmodel.Actions.Action;
import com.vztekoverflow.lospiratos.viewmodel.Resource;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class BuyCommodity extends CommodityTransaction {

    @Override
    protected Action createCopy() {
        return new BuyCommodity();
    }

    @Override
    public void performOnTarget() {
        throw new NotImplementedException();
    }

    @Override
    public String getČeskéJméno() {
        return "nákup komodit";
    }

    @Override
    protected Resource recomputeCost() {
        throw new NotImplementedException();
    }

}
