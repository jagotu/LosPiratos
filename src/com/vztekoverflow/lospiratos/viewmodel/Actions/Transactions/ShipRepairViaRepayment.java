package com.vztekoverflow.lospiratos.viewmodel.Actions.Transactions;

import com.vztekoverflow.lospiratos.viewmodel.Actions.Action;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class ShipRepairViaRepayment extends ShipRepair {
    @Override
    protected Action createCopy() {
        return new ShipRepairViaRepayment();
    }

    @Override
    public void performOnTarget() {
        throw new NotImplementedException();
    }

    @Override
    public String getČeskéJméno() {
        return "oprava lodě pomocí zaplacení";
    }
}
