package com.vztekoverflow.lospiratos.viewmodel.Actions.Transactions ;

import com.vztekoverflow.lospiratos.viewmodel.Actions.Action;
import com.vztekoverflow.lospiratos.viewmodel.Actions.Transaction;
import com.vztekoverflow.lospiratos.viewmodel.ResourceImmutable;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class SellCommodity extends Transaction {
    private final ResourceImmutable commoditiesForSale;

    public SellCommodity(ResourceImmutable commoditiesForSale) {
        this.commoditiesForSale = commoditiesForSale;
    }

    public final ResourceImmutable getCommoditiesForSale() {
        return commoditiesForSale;
    }

    @Override
    protected Action createCopy() {
        return new SellCommodity(commoditiesForSale);
    }

    @Override
    public void performOnTarget() {
        throw new NotImplementedException();
    }

    @Override
    public String getČeskéJméno() {
        return "prodej komodit";
    }
}
