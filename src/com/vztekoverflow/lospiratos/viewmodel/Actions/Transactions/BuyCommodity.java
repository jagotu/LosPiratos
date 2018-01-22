package com.vztekoverflow.lospiratos.viewmodel.Actions.Transactions ;

import com.vztekoverflow.lospiratos.viewmodel.Actions.Action;
import com.vztekoverflow.lospiratos.viewmodel.Actions.Transaction;
import com.vztekoverflow.lospiratos.viewmodel.ResourceImmutable;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class BuyCommodity extends Transaction {
    private final ResourceImmutable commoditiesToBuy;

    public BuyCommodity(ResourceImmutable commoditiesToBuy) {
        this.commoditiesToBuy = commoditiesToBuy;
    }

    public final ResourceImmutable getCommoditiesToBuy() {
        return commoditiesToBuy;
    }

    @Override
    protected Action createCopy() {
        return new BuyCommodity(commoditiesToBuy);
    }

    @Override
    public void performOnTarget() {
        throw new NotImplementedException();
    }

    @Override
    public String getČeskéJméno() {
        return "nákup komodit";
    }
}
