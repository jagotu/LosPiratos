package com.vztekoverflow.lospiratos.viewmodel.Actions.Transactions ;

import com.vztekoverflow.lospiratos.viewmodel.Actions.Action;
import com.vztekoverflow.lospiratos.viewmodel.Actions.Transaction;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipEnhancement;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class BuyNewEnhancement extends Transaction {
    private final Class<? extends ShipEnhancement> enhancement;

    public BuyNewEnhancement(Class<? extends ShipEnhancement> enhancement) {
        this.enhancement = enhancement;
    }

    public final Class<? extends ShipEnhancement> getEnhancement() {
        return enhancement;
    }

    @Override
    protected Action createCopy() {
        return new BuyNewEnhancement(enhancement);
    }

    @Override
    public void performOnTarget() {
        throw new NotImplementedException();
    }

    @Override
    public String getČeskéJméno() {
        return "zakoupení vylepšení";
    }
}
